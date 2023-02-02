package com.xzy.wechatbot.client;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xzy.wechatbot.common.WechatBotCommon;
import com.xzy.wechatbot.domain.WechatMsg;
import com.xzy.wechatbot.domain.WechatReceiveMsg;
import com.xzy.wechatbot.enums.MsgTypeEnum;
import com.xzy.wechatbot.util.HttpClient;
import com.xzy.wechatbot.vo.MsgVO;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

public class WechatBotClient extends WebSocketClient implements WechatBotCommon {

    private static String wechatMessageUrl;

    public static void setWechatMessageUrl(String wechatMessageUrl) {
        WechatBotClient.wechatMessageUrl = wechatMessageUrl;
    }

    public WechatBotClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("已发送尝试连接到微信客户端请求");
    }

    @Override
    public void onMessage(String msg) {
        //异步返回响应
        WechatReceiveMsg wechatReceiveMsg = JSONObject.parseObject(msg, WechatReceiveMsg.class);
        MsgTypeEnum msgType = MsgTypeEnum.findByValue(wechatReceiveMsg.getType());
        if (msgType == null) {
            return;
        }
        switch (msgType) {
            case USER_LIST:
                MsgVO.setAllList(msg);
                MsgVO.setHasGetAllList(true);
                break;
            case CHATROOM_MEMBER:
                MsgVO.setRoomListWithMember(msg);
                MsgVO.setHasGetRoomListWithMember(true);
                break;
            case CHATROOM_MEMBER_NICK:
                MsgVO.setMemDetail(msg);
                MsgVO.setHasGetMemDetail(true);
                break;
            case RECV_TXT_MSG:
                //异步发送文字消息给MQ，另一台机器进行订阅消费
                HttpClient.doPost(wechatMessageUrl + "/wechat-rsv-msg/txt", JSON.toJSONString(wechatReceiveMsg));
                break;
            case RECV_PIC_MSG:
                //异步发送图片消息给MQ，另一台机器进行订阅消费
                HttpClient.doPost(wechatMessageUrl + "/wechat-rsv-msg/pic", JSON.toJSONString(wechatReceiveMsg));
                break;
        }
        //除了RECV_TXT_MSG和RECV_PIC_MSG还有没有另外的东西呢？=_=
        //如果想做成智能机器人的话，这里也可以进行直接发送WebSocket，如Q&A
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("已断开连接... ");
    }

    @Override
    public void onError(Exception e) {
        System.err.println("通信连接出现异常:" + e.getMessage());
    }

    /**
     * 发送消息工具
     */
    public void sendMsgUtil(WechatMsg wechatMsg) {
        //把""和null都变成"null"
        wechatMsg.setExt(StringUtils.hasText(wechatMsg.getExt()) ? wechatMsg.getExt() : NULL_MSG);
        wechatMsg.setNickname(StringUtils.hasText(wechatMsg.getNickname()) ? wechatMsg.getNickname() : NULL_MSG);
        wechatMsg.setRoomid(StringUtils.hasText(wechatMsg.getRoomid()) ? wechatMsg.getRoomid() : NULL_MSG);
        wechatMsg.setContent(StringUtils.hasText(wechatMsg.getContent()) ? wechatMsg.getContent() : NULL_MSG);
        wechatMsg.setWxid(StringUtils.hasText(wechatMsg.getWxid()) ? wechatMsg.getWxid() : NULL_MSG);
        // 消息Id
        wechatMsg.setId(String.valueOf(System.currentTimeMillis()));
        // 发送消息
        send(JSONObject.toJSONString(wechatMsg));
    }
}