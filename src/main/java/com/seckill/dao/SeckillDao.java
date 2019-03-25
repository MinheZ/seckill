package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    /**
     * @Description:减库存
     * @Param: [seckillId, killTime]
     * @return: 如果影响行数 >1 ，表示更新的记录行数
     * @Author: MinheZ
     * @Date: 2019/3/25
     **/
    int reduceNumber(@Param("seckillId")long seckillId, @Param("killTime") Date killTime);

    /**
     * @Description: 根据 ID 查秒杀对象
     * @Param: [seckillId]
     * @return: com.seckill.entity.Seckill
     * @Author: MinheZ
     * @Date: 2019/3/25
     **/
    Seckill queryById(long seckillId);

    /**
     * @Description: 根据偏移量查询秒杀商品列表
     * @Param: [offset, limit]
     * @return: java.util.List<com.seckill.entity.Seckill>
     * @Author: MinheZ
     * @Date: 2019/3/25
     **/
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit")int limit);

}
