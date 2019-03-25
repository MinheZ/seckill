package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnums;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseExeception;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @program: seckill
 * @description:
 * @author: MinheZ
 * @create: 2019-03-25 19:45
 **/

@Service
public class SeckillServiceImpl implements SeckillService {

    // 日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;
    // 盐值字符串，目的是混淆md5
    private final String slat = "HO@!IW&^*&^D)W)_(_)ci9w089c79wcl(*^68";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        // 判断秒杀时间是否合法
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckill.getSeckillId(), nowTime.getTime(),
                    startTime.getTime(), endTime.getTime());
        }
        // 生成 md5，转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    @Transactional
    /*
    * 使用注解控制事务方法的优点：
    *   1. 开发团队达成一致约定，明确标注事务方法的编程风格。
    *   2. 保证事务方法的执行时间尽可能短，不要穿插其它网络操作，RPC/HTTP请求/或者剥离到事务方法外部
    *   3. 不是所有的方法都需要事务，只有一条修改操作，只读操作不需要事务控制
    * */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseExeception, RepeatKillException {
        if (md5 == null || md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data had been modified!");
        }
        // 执行秒杀逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();
        try {
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                // 没有更新记录
                throw new SeckillCloseExeception("Seckill is closed!");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                // 唯一：seckillId, userPhone
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated!");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnums.SUCCESS, successKilled);
                }
            }
        // 先显示抛出已知异常
        } catch (SeckillCloseExeception seckillCloseExeception) {
            throw seckillCloseExeception;
        } catch (RepeatKillException repeatKillException) {
            throw repeatKillException;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译器异常，转化为运行期异常
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }
}
