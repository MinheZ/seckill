package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseExeception;
import com.seckill.exception.SeckillException;

import java.util.List;

/**
 * @program: seckill
 * @description: 业务接口：站在“使用者”角度设计接口
 * 三方面：方法定义的粒度，参数，返回类型（return 类型/异常）
 * @author: MinheZ
 * @create: 2019-03-25 19:11
 **/

public interface SeckillService {

    /**
     * @Description: 查询所有秒杀记录
     * @Param: []
     * @return: java.util.List<com.seckill.entity.Seckill>
     * @Author: MinheZ
     * @Date: 2019/3/25
     **/
    List<Seckill> getSeckillList();

    /**
     * @Description: 查询单个秒杀记录
     * @Param: [seckillId]
     * @return: com.seckill.entity.Seckill
     * @Author: MinheZ
     * @Date: 2019/3/25
     **/
    Seckill getById(long seckillId);

    /**
     * @Description: 秒杀开启是输出秒杀接口地址， 否则输出系统时间和秒杀时间
     * @Param: [seckillId]
     * @return: void
     * @Author: MinheZ
     * @Date: 2019/3/25
    **/
    Exposer exportSeckillUrl(long seckillId);
    
    /** 
     * @Description: 执行秒杀操作 
     * @Param: [seckillId, userPhone, md5] 
     * @return: SeckillExecution
     * @Author: MinheZ
     * @Date: 2019/3/25 
    **/ 
    SeckillExecution executeSeckill (long seckillId, long userPhone, String md5) throws SeckillException
                                    , SeckillCloseExeception, RepeatKillException;
}
