package com.xzy.wechatbot.service.impl;

import com.xzy.wechatbot.client.WechatBotClient;
import com.xzy.wechatbot.common.WechatBotCommon;
import com.xzy.wechatbot.domain.WechatMsg;
import com.xzy.wechatbot.service.WechatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatBotServiceImpl implements WechatBotService, WechatBotCommon {

    @Autowired
    private WechatBotClient wechatBotClient;

    @Override
    public void wechatCommon(WechatMsg wechatMsg) {
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    @Override
    public void sendTextMsg(WechatMsg wechatMsg) {
        wechatMsg.setType(TXT_MSG);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    @Override
    public void sendImgMsg(WechatMsg wechatMsg) {
        wechatMsg.setType(PIC_MSG);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    @Override
    public void sendATMsg(WechatMsg wechatMsg) {
        wechatMsg.setType(AT_MSG);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    @Override
    public void sendAnnex(WechatMsg wechatMsg) {
        wechatMsg.setType(ATTATCH_FILE);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    @Override
    public void getWeChatUserList() {
        WechatMsg wechatMsg = new WechatMsg();
        wechatMsg.setType(USER_LIST);
        wechatMsg.setContent(CONTACT_LIST);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    @Override
    public void getChatroomMemberNick(String roomid, String wxid) {
        WechatMsg wechatMsg = new WechatMsg();
        wechatMsg.setRoomid(roomid);
        wechatMsg.setWxid(wxid);
        wechatMsg.setType(CHATROOM_MEMBER_NICK);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

    @Override
    public void getMemberId() {
        WechatMsg wechatMsg = new WechatMsg();
        wechatMsg.setType(CHATROOM_MEMBER);
        wechatBotClient.sendMsgUtil(wechatMsg);
    }

}
