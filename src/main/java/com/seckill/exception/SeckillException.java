package com.seckill.exception;

/**
 * @program: seckill
 * @description: 秒杀相关异常
 * @author: MinheZ
 * @create: 2019-03-25 19:39
 **/

public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
