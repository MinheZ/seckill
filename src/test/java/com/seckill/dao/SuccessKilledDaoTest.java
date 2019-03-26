package com.seckill.dao;


import com.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilledTest() {
        long id = 1001L;
        long phone = 11111111111L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithSeckillTest() {
        long id = 1001L;
        long phone = 11111111111L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled + "\n" + successKilled.getSeckill());
    }
}