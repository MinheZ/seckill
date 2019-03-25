package com.seckill.dao;

import com.seckill.entity.SuccessKilled;

/**
 * @program: seckill
 * @description:
 * @author: Mr.Wang
 * @create: 2019-03-25 14:23
 **/

public interface SuccessKilledDao {
    /**
     * @Description: 插入购买明细，可过滤重复 （联合唯一主键）
     * @Param: [seckillId, userPhone]
     * @return: int 插入的行数
     * @Author: MinheZ
     * @Date: 2019/3/25
     **/
    int insertSuccessKilled(long seckillId, long userPhone);

    /**
     * @Description: 根据 ID 查询 SuccessKilled 并携带 seckill 对象
     * @Param: [seckillId]
     * @return: com.seckill.entity.SuccessKilled
     * @Author: MinheZ
     * @Date: 2019/3/25
     **/
    SuccessKilled queryByIdWithSeckill(long seckillId);

}
