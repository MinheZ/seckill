package com.seckill.dto;

import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnums;

/**
 * @program: seckill
 * @description: 封装秒杀执行后的结果
 * @author: MinheZ
 * @create: 2019-03-25 19:30
 **/

public class SeckillExecution {
    private long seckillId;

    // 秒杀执行结果状态
    private int state;

    // 状态表示
    private String stateInfo;

    // 秒杀成功对象
    private SuccessKilled successKilled;

    // 如果秒杀失败
    public SeckillExecution(long seckillId, SeckillStateEnums stateEnums) {
        this.seckillId = seckillId;
        this.state = stateEnums.getState();
        this.stateInfo = stateEnums.getStateInfo();
    }

    // 如果秒杀成功
    public SeckillExecution(long seckillId, SeckillStateEnums stateEnums, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = stateEnums.getState();
        this.stateInfo = stateEnums.getStateInfo();
        this.successKilled = successKilled;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
