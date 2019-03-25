package com.seckill.dao;


import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/ApplicationContext-dao.xml"})
public class SeckillDaoTest {

    // 注入依赖
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testQueryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }
    /*
    * org.mybatis.spring.MyBatisSystemException:
    * nested exception is org.apache.ibatis.binding.BindingException:
    * Parameter 'offset' not found. Available parameters are [arg1, arg0, param1, param2]
    * java 没有保存形参的记录  使用 @Param("offset")注解的方式
    * */
    @Test
    public void testQueryAll() throws Exception {
        List<Seckill> list = seckillDao.queryAll(0, 100);
        for (Seckill seckill : list)
            System.out.println(seckill);
    }

    @Test
    public void testReduceNumber() throws Exception {
        Date killDate = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killDate);
        System.out.println("updateCount: " + updateCount);
    }
}