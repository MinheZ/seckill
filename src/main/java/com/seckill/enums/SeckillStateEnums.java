package com.seckill.enums;

/**
 * @program: seckill
 * @description: 使用枚举表述常量数据
 * @author: MinheZ
 * @create: 2019-03-25 20:23
 **/

public enum SeckillStateEnums {

    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改");

    private int state;

    private String stateInfo;

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    SeckillStateEnums(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillStateEnums stateOf(int index) {
        for (SeckillStateEnums state : values()) {
            if (state.getState() == index)
                return state;
        }
        return null;
    }
}
