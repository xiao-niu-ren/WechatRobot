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
        // 由于我的机器人是放在某个小服务器上的, 就将接收数据后的处理交给了另外一个服务器(看群里好多群友也这么干的)所以我这里就加了这几行代码,这根据自己的想法进行自定义

        // 这里也可以不进行转换 直接将微信中接收到的消息交给服务端, 提高效率,但是浪费在网络通信上的资源相对来说就会变多(根据自己需求自信来写没什么特别的)
        System.out.println("微信中收到了消息:" + msg);

        //xzy
        //如果返回的是AllUserList则将hasGetAllList置位true，并将结果反给String
        if(WechatBotCommon.USER_LIST.equals(JSONObject.parseObject(msg, WechatReceiveMsg.class).getType())){
            MsgVO.allList = msg;
            MsgVO.hasGetAllList = true;
        }
        //如果返回的是
        if(WechatBotCommon.CHATROOM_MEMBER.equals(JSONObject.parseObject(msg, WechatReceiveMsg.class).getType())){
            MsgVO.roomListWithMember = msg;
            MsgVO.hasGetRoomListWithMember = true;
        }
        //如果返回的是
        if(WechatBotCommon.CHATROOM_MEMBER_NICK.equals(JSONObject.parseObject(msg, WechatReceiveMsg.class).getType())){
            MsgVO.memDetail = msg;
            MsgVO.hasGetMemDetail = true;
        }

        // 是否开启远程处理消息功能
        if (WechatBotConfig.wechatMsgServerIsOpen) {
            // 不等于心跳包
            WechatReceiveMsg wechatReceiveMsg = JSONObject.parseObject(msg, WechatReceiveMsg.class);
            if (!WechatBotCommon.HEART_BEAT.equals(wechatReceiveMsg.getType())) {
                HttpUtil.post(WechatBotConfig.wechatMsgServerUrl, msg);
            }
        }
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
        String string = JSONObject.toJSONString(wechatMsg);
        System.err.println(":" + string);
        send(JSONObject.toJSONString(wechatMsg));
    }
}