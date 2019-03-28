package com.seckill.dao.cache;

import com.seckill.dao.SeckillDao;
import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-redis.xml"})
public class JedisClientClusterTest {

    @Autowired
    private JedisClientCluster jedisClient;

    @Autowired
    private SeckillDao seckillDao;

    private Long id = 1003L;

    @Test
    public void set() {
        Seckill seckill = jedisClient.get(id);
        if (seckill == null) {
            seckill = seckillDao.queryById(id);
            if (seckill != null) {
                String key = jedisClient.set(seckill);
                System.out.println(key);
                seckill = jedisClient.get(seckill.getSeckillId());
                System.out.println(seckill);
            }
        }
        System.out.println(seckill);
    }

    @Test
    public void get() {
    }
}