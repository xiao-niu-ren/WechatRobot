package com.xzy.wechatbot.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信消息接收对象 t_wechat_receive_msg
 */
@Data
public class WechatReceiveMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 微信消息编号 */
    private Long wechatReceiveMsgId;
    /** 消息id */
    private String id;
    /** 消息内容 */
    private String content;
    /** 群组里消息发送人,  私人对话这个字段为空 */
    private String id1;
    /** 群组里消息为 群组id, 个人的对话 为个人微信id */
    private String id2;
    /** srv_id */
    private Long srvid;
    /** 接收消息时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /** 接收消息类型 */
    private Integer type;
    /** 发送消息得对话框id   个人是个人得微信id,群组是群组得id带 */
    private String wxid;
}
