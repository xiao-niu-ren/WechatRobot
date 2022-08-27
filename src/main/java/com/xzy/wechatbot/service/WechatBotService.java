package com.xzy.wechatbot.service;

import com.xzy.wechatbot.domain.WechatMsg;

public interface WechatBotService {

    public void wechatCommon(WechatMsg wechatMsg);

    public void sendTextMsg(WechatMsg wechatMsg);

    public void sendImgMsg(WechatMsg wechatMsg);

    public void sendATMsg(WechatMsg wechatMsg);

    public void sendAnnex(WechatMsg wechatMsg);

    public void getWeChatUserList();

    public void getChatroomMemberNick(String roomid, String wxid);

    public void getMemberId();

}
