package com.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

@Service
public class JedisClientCluster{

    @Autowired
    private JedisCluster jedisCluster;

    // 日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 序列化
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    // JedisCluster 本身就采用了池化技术，因此不需要 jedisCluster.close();
    public String set(Seckill seckill) {
        try {
            String key = "seckill:" + seckill.getSeckillId();
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            // 超时缓存
            int timeout = 60 * 60;  // 1小时

            String result = jedisCluster.setex(key, timeout, bytes.toString());
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public Seckill get(Long seckillId) {
        try {
            String key = "seckill:" + seckillId;
            byte[] bytes = jedisCluster.get(key).getBytes();
            if (bytes != null) {
                Seckill seckill = schema.newMessage();
                ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                return seckill;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

//    public Boolean exists(String key) {
//        return jedisCluster.exists(key);
//    }
//
//    public Long expire(String key, int seconds) {
//        return jedisCluster.expire(key, seconds);
//    }
//
//    public Long ttl(String key) {
//        return jedisCluster.ttl(key);
//    }
//
//    public Long incr(String key) {
//        return jedisCluster.incr(key);
//    }
//
//    public Long hset(String key, String field, String value) {
//        return jedisCluster.hset(key, field, value);
//    }
//
//    public String hget(String key, String field) {
//        return jedisCluster.hget(key, field);
//    }
//
//    public Long hdel(String key, String... field) {
//        return jedisCluster.hdel(key, field);
//    }

}
