package com.seckill.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/ApplicationContext-service.xml",
        "classpath:spring/ApplicationContext-dao.xml"})
public class SeckillServiceTest {

//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private SeckillService seckillService;
//
//    @Test
//    public void getSeckillListTest() {
//        List<Seckill> list = seckillService.getSeckillList();
//        logger.info("list={}", list);
//    }
//    @Test
//    public void getByIdTest() {
//        long id = 1000L;
//        Seckill seckill = seckillService.getById(id);
//        logger.info("seckill={}",seckill);
//    }
//    @Test
//    public void exportSeckillLogicTest() {
//        long id = 1000L;
//        Exposer exposer = seckillService.exportSeckillUrl(id);
//        if (exposer.isExposed()) {
//            logger.info("exposer={}",exposer);
//            long phone = 12345678902L;
//            String md5 = exposer.getMd5();
//            try {
//                SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
//                logger.info("result={}", execution);
//            }catch (RepeatKillException r) {
//                logger.error(r.getMessage());
//            }catch (SeckillCloseException seckillCloseException) {
//                logger.error(seckillCloseException.getMessage());
//            }
//        }else {
//            logger.warn("exposer={}", exposer);
//        }
//
//    }
}