package com.xzy.wechatbot.service.impl;

import com.xzy.wechatbot.client.WechatBotClient;
import com.xzy.wechatbot.common.WechatBotCommon;
import com.xzy.wechatbot.domain.WechatMsg;
import com.xzy.wechatbot.service.WechatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class WechatBotServiceImpl implements WechatBotService, WechatBotCommon {


    /** 注入微信客户端 */
    @Autowired
    private WechatBotClient wechatBotClient;

    /**
     * 描述: 发送文字消息
     *
     * @param wechatMsg 微信消息体
     * @return void
     */
    @Override
    public void wechatCommon(WechatMsg wechatMsg) {
        // 消息类型
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    /**
     * 描述: 发送文字消息
     *
     * @param wechatMsg 微信消息体
     * @return void
     */
    @Override
    public void sendTextMsg(WechatMsg wechatMsg) {
        wechatMsg.setType(TXT_MSG);
        // 消息类型
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    /**
     * 描述: 发送图片消息
     *
     * @param wechatMsg 微信消息体
     * @return void
     */
    @Override
    public void sendImgMsg(WechatMsg wechatMsg) {
        wechatMsg.setType(PIC_MSG);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    /**
     * 描述: 群组内发送@指定人消息
     *
     * @param wechatMsg
     */
    @Override
    public void sendATMsg(WechatMsg wechatMsg) {
        wechatMsg.setType(AT_MSG);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }


    /**
     * 描述: 发送附件
     *
     * @param wechatMsg
     */
    @Override
    public void sendAnnex(WechatMsg wechatMsg) {
        wechatMsg.setType(ATTATCH_FILE);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }


    /**
     * 描述: 获取微信群组,联系人列表
     *
     * @param
     * @see WechatBotCommon#USER_LIST 发起后会收到一条type类型是该常量值消息
     */
    @Override
    public void getWeChatUserList() {
        WechatMsg wechatMsg = new WechatMsg();
        wechatMsg.setType(USER_LIST);
        wechatMsg.setContent(CONTACT_LIST);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

//    /**
//     * 描述:获取指定联系人的详细信息
//     *
//     * @param wxid 被获取详细信息的人的 微信id
//     * @return void
//     */
//    @Override
//    public void getPersonalDetail(String wxid) {
//        WechatMsg wechatMsg = new WechatMsg();
//        wechatMsg.setType(PERSONAL_DETAIL);
//        wechatBotClient.sendMsgUtil(wechatMsg);
//    }

    /**
     * 描述: 获取群组里指定联系人的详细信息
     *
     * @param roomid 群组id
     * @param wxid   指定用户id
     */
    @Override
    public void getChatroomMemberNick(String roomid, String wxid) {
        WechatMsg wechatMsg = new WechatMsg();
        wechatMsg.setRoomid(roomid);
        wechatMsg.setWxid(wxid);
        wechatMsg.setType(CHATROOM_MEMBER_NICK);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }


    /**
     * 描述: 获取所有群组以及成员
     */
    @Override
    public void getMemberId() {
        WechatMsg wechatMsg = new WechatMsg();
        wechatMsg.setType(CHATROOM_MEMBER);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }
}
