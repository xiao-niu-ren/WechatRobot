package com.xzy.wechatbot.client;

import cn.hutool.core.img.gif.AnimatedGifEncoder;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.xzy.wechatbot.common.WechatBotCommon;
import com.xzy.wechatbot.common.WechatBotConfig;
import com.xzy.wechatbot.controller.WechatBotController;
import com.xzy.wechatbot.domain.WechatMsg;
import com.xzy.wechatbot.domain.WechatReceiveMsg;
import com.xzy.wechatbot.vo.MsgVO;
import javafx.scene.control.RadioMenuItem;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

/**
 * websocket机器人客户端
 */
public class WechatBotClient extends WebSocketClient implements WechatBotCommon {


    /**
     * 描述: 构造方法创建 WechatBotClient对象
     *
     * @param url WebSocket链接地址
     * @return
     */
    public WechatBotClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    /**
     * 描述: 在websocket连接开启时调用
     *
     * @param serverHandshake
     * @return void
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.err.println("已发送尝试连接到微信客户端请求");
    }

    /**
     * 描述: 方法在接收到消息时调用
     *
     * @param msg
     */
    @Override
    public void onMessage(String msg) {

//        System.out.println("微信中收到了消息:" + msg);

        //xzy
        //异步返回响应
        if (WechatBotCommon.USER_LIST.equals(JSONObject.parseObject(msg, WechatReceiveMsg.class).getType())) {
            MsgVO.setAllList(msg);
            MsgVO.setHasGetAllList(true);
        } else if (WechatBotCommon.CHATROOM_MEMBER.equals(JSONObject.parseObject(msg, WechatReceiveMsg.class).getType())) {
            MsgVO.setRoomListWithMember(msg);
            MsgVO.setHasGetRoomListWithMember(true);
        } else if (WechatBotCommon.CHATROOM_MEMBER_NICK.equals(JSONObject.parseObject(msg, WechatReceiveMsg.class).getType())) {
            MsgVO.setMemDetail(msg);
            MsgVO.setHasGetMemDetail(true);
        } else if (WechatBotCommon.RECV_TXT_MSG.equals(JSONObject.parseObject(msg, WechatReceiveMsg.class).getType())) {
            //异步发送文字消息给MQ，另一台机器进行订阅消费
            System.out.println(msg);
        } else if (WechatBotCommon.RECV_PIC_MSG.equals(JSONObject.parseObject(msg, WechatReceiveMsg.class).getType())) {
            //异步发送图片消息给MQ，另一台机器进行订阅消费
            System.out.println(msg);
        }
        //除了RECV_TXT_MSG和RECV_PIC_MSG还有没有另外的东西呢？=_=

        //如果想做成智能机器人的话，这里也可以进行直接发送WebSocket，如Q & A

//        是否开启远程处理消息功能
//        if (WechatBotConfig.wechatMsgServerIsOpen) {
//            // 不等于心跳包
//            WechatReceiveMsg wechatReceiveMsg = JSONObject.parseObject(msg, WechatReceiveMsg.class);
//            if (!WechatBotCommon.HEART_BEAT.equals(wechatReceiveMsg.getType())) {
//                HttpUtil.post(WechatBotConfig.wechatMsgServerUrl, msg);
//            }
//        }
    }

    /**
     * 描述: 方法在连接断开时调用
     *
     * @param i
     * @param s
     * @param b
     * @return void
     */
    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("已断开连接... ");
    }

    /**
     * 描述: 方法在连接出错时调用
     *
     * @param e
     * @return void
     */
    @Override
    public void onError(Exception e) {
        System.err.println("通信连接出现异常:" + e.getMessage());
    }

    /**
     * 描述: 发送消息工具 (其实就是把几行常用代码提取出来 )
     *
     * @param wechatMsg 消息体
     * @return void
     */
    public void sendMsgUtil(WechatMsg wechatMsg) {
        //把""和null都变成null
        if (!StringUtils.hasText(wechatMsg.getExt())) {
            wechatMsg.setExt(NULL_MSG);
        }
        if (!StringUtils.hasText(wechatMsg.getNickname())) {
            wechatMsg.setNickname(NULL_MSG);
        }
        if (!StringUtils.hasText(wechatMsg.getRoomid())) {
            wechatMsg.setRoomid(NULL_MSG);
        }
        if (!StringUtils.hasText(wechatMsg.getContent())) {
            wechatMsg.setContent(NULL_MSG);
        }
        if (!StringUtils.hasText(wechatMsg.getWxid())) {
            wechatMsg.setWxid(NULL_MSG);
        }
        // 消息Id
        wechatMsg.setId(String.valueOf(System.currentTimeMillis()));
        // 发送消息
        //下面的log先注释了
//        String string = JSONObject.toJSONString(wechatMsg);
//        System.err.println(":" + string);
        send(JSONObject.toJSONString(wechatMsg));
    }
}