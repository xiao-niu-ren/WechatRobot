package com.xzy.wechatbot.enums;

/**
 * @description: MsgTypeEnum
 * @author: Xie zy
 * @create: 2022.09.30
 */
public enum MsgTypeEnum {
    USER_LIST(5000),
    CHATROOM_MEMBER(5010),
    CHATROOM_MEMBER_NICK(5020),
    RECV_TXT_MSG(1),
    RECV_PIC_MSG(3),
    ;
    Integer value;


    MsgTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static MsgTypeEnum findByValue(Integer value) {
        for (MsgTypeEnum msgTypeEnum : MsgTypeEnum.values()) {
            if (msgTypeEnum.value.equals(value)) {
                return msgTypeEnum;
            }
        }
        return null;
    }
}
