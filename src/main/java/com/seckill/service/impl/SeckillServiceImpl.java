package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dao.cache.JedisClientCluster;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    // 日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private JedisClientCluster jedisClient;

    // 加入一个混淆字符串(秒杀接口)的salt，为了避免用户猜出我们的md5值，值任意给，越复杂越好，不可逆
    private final String salt = "Hjdipueh7*&^*&%dhulkne123";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * @Description: 是否暴露秒杀地址
     * @Param: [seckillId]
     * @return: com.seckill.dto.Exposer
     * @Author: MinheZ
     * @Date: 2019/3/26
     **/
    public Exposer exportSeckillUrl(long seckillId) {
        // 优化点：缓存优化，一致性维护在超时的基础上
        // 1. 先访问缓存，但是不能影响正常业务
        try {
            Seckill seckill = redisDao.getSeckill(seckillId);
            if (seckill == null) {
                // 缓存为空，则访问数据库
                seckill = seckillDao.queryById(seckillId);
                if (seckill == null) {  // 数据库也未命中
                    return new Exposer(false, seckillId);
                } else {
                    try {
                        redisDao.putSeckill(seckill);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    return isExportUrl(seckill);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {  // 数据库也未命中
            return new Exposer(false, seckillId);
        } else {
            return isExportUrl(seckill);
        }
    }

    private Exposer isExportUrl(Seckill seckill) {
        Date startTime = seckill.getStartTime();    // 秒杀开始时间
        Date endTime = seckill.getEndTime();        // 秒杀结束时间
        Date nowTime = new Date();                  // 当前时间

        // 判断当前时间是否在秒杀时间之内
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckill.getSeckillId(), nowTime.getTime(),
                    startTime.getTime(), endTime.getTime());
        }
        // MD5不可逆
        String md5 = getMD5(seckill.getSeckillId());
        return new Exposer(true, md5, seckill.getSeckillId());
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill had been modified!");
        }
        // 执行秒杀业务逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();
        try {
            // 记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated!");
            } else {
                // 减库存，热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // rollback
                    throw new SeckillCloseException("seckill is closed!");
                } else {
                    // 秒杀成功，commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (RepeatKillException r) {
            logger.error(r.getMessage(), r);
            throw r;
        } catch (SeckillCloseException c) {
            logger.error(c.getMessage(), c);
            throw c;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStatEnum.DATE_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        // 执行存储过程，result被赋值
        try {
            seckillDao.killByProcedure(map);
            // 获取 result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            // 调用存储过程出异常
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}