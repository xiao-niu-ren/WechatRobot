package com.xzy.wechatbot.service;

import com.xzy.wechatbot.domain.WechatMsg;

/**
 *
 */
public interface WechatBotService {

    /**
     * 描述: 发送文字消息
     *
     * @param wechatMsg 微信消息体
     * @return void
     */
    public void wechatCommon(WechatMsg wechatMsg);

    /**
     * 描述: 发送文字消息
     *
     * @param wechatMsg 微信消息体
     * @return void
     */
    public void sendTextMsg(WechatMsg wechatMsg);

    /**
     * 描述: 发送图片消息
     *
     * @param wechatMsg 微信消息体
     * @return void
     */
    public void sendImgMsg(WechatMsg wechatMsg);

    /**
     * 描述: 群组内发送@指定人消息
     *
     * @param wechatMsg
     */
    void sendATMsg(WechatMsg wechatMsg);


    /**
     * 描述: 发送附件
     *
     * @param wechatMsg

     */
    void sendAnnex(WechatMsg wechatMsg);

    /**
     * 描述: 获取微信群组,联系人列表
     *
     * @param

     */
    void getWeChatUserList();

//    /**
//     * 描述:获取指定联系人的详细信息
//     *
//     * @param wxid 被获取详细信息的人的 微信id
//     * @return void
//     */
//    void getPersonalDetail(String wxid);

    /**
     * 描述: 获取群组里指定联系人的详细信息
     *
     * @param roomid 群组id
     * @param wxid   指定用户id

     */
    void getChatroomMemberNick(String roomid, String wxid);

    /**
     * 描述: 获取所有群组以及成员
     *
     */
    void getMemberId();
}
