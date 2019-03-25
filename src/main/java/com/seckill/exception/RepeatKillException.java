package com.seckill.exception;

/**
 * @program: seckill
 * @description: 重复秒杀异常（运行期异常）
 * @author: MinheZ
 * @create: 2019-03-25 19:34
 **/

public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(String message) {
        super(message);
    }
}
