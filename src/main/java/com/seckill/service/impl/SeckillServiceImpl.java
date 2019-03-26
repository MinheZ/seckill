package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {
    // 日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    // 加入一个混淆字符串(秒杀接口)的salt，为了避免用户猜出我们的md5值，值任意给，越复杂越好，不可逆
    private final String slat = "Hjdipueh7*&^*&%dhulkne123";

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
        Seckill seckill = seckillDao.queryById(seckillId);
        // 如果查询到的秒杀产品为空，则直接返回一个 Exposer
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();    // 秒杀开始时间
        Date endTime = seckill.getEndTime();        // 秒杀结束时间
        Date nowTime = new Date();                  // 当前时间

        // 判断当前时间是否在秒杀时间之内
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(),
                    startTime.getTime(), endTime.getTime());
        }

        String md5 = null;
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(Long seckillId) {
        
    }

    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        return null;
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        return null;
    }
}
