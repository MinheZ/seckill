package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-service.xml",
        "classpath:spring/spring-dao.xml"})
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillListTest() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }
    @Test
    public void getByIdTest() {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }
    @Test
    public void exportSeckillLogicTest() {
        long id = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}",exposer);
            long phone = 12345678902L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}", execution);
            }catch (RepeatKillException r) {
                logger.error(r.getMessage());
            }catch (SeckillCloseException seckillCloseException) {
                logger.error(seckillCloseException.getMessage());
            }
        }else {
            logger.warn("exposer={}", exposer);
        }
    }
    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1001L;
        long phone = 12345678903L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            logger.info("exposer={}",exposer);
            String md5 = exposer.getMd5();
                SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
                logger.info(execution.getStateInfo());
        }
    }

    @Test
    public void tryCatch() {
        int a = 1;
        int b = 2;
        int c;
        try {
            c = a / 0;
            System.out.println(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(a + b);
    }
}