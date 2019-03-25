package com.seckill.exception;

/**
 * @program: seckill
 * @description: 秒杀关闭异常
 * @author: MinheZ
 * @create: 2019-03-25 19:36
 **/

public class SeckillCloseExeception extends SeckillException{

    public SeckillCloseExeception(String message) {
        super(message);
    }

    public SeckillCloseExeception(String message, Throwable cause) {
        super(message, cause);
    }
}
