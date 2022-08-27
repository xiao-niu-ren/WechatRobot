package com.xzy.wechatbot.controller;

import com.xzy.wechatbot.vo.AjaxResult;
import com.xzy.wechatbot.domain.WechatMsg;
import com.xzy.wechatbot.service.WechatBotService;
import com.xzy.wechatbot.vo.MsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 获取的方法：
 * /getWeChatUserList                         GET|获取所有联系人列表，包括微信好友、群组、公众号
 * /getMemberId                               GET|获取所有群组列表及该群组所有群成员id
 * /getChatroomMemberNick/{roomid}/{wxid}     GET|根据群组id和该组某成员id查询该成员群昵称
 *
 * 发送的方法：
 * /xiaoniuren                                GET or POST|给小牛人发消息
 * /wechatCommon                              POST|发送通用消息
 * /sendTextMsg                               POST|发送文本消息
 * /sendImgMsg                                POST|发送图片消息
 * /sendATMsg                                 POST|发送at消息
 * /sendAnnex                                 POST|发送附件消息
 */
@RestController
public class WechatBotController {

    @Autowired
    private WechatBotService wechatBotService;

    @Value("${xiaoniuren.wxid}")
    private String XIAO_NIU_REN_WX_ID;

    @PostMapping("/wechatCommon")
    public AjaxResult wechatCommon(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.wechatCommon(wechatMsg);
        return AjaxResult.success();
    }

    @PostMapping("/sendTextMsg")
    public AjaxResult sendTextMsg(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.sendTextMsg(wechatMsg);
        return AjaxResult.success();
    }

    @RequestMapping("/xiaoniuren")
    public AjaxResult xiaoniuren(@RequestParam(required = false) String msg,@RequestBody(required = false) WechatMsg wechatMsg) {
        if(!StringUtils.isEmpty(msg)){
            WechatMsg msgToSend = new WechatMsg();
            msgToSend.setWxid(XIAO_NIU_REN_WX_ID);
            msgToSend.setContent(msg);
            wechatBotService.sendTextMsg(msgToSend);
        } else {
            wechatBotService.sendTextMsg(wechatMsg);
        }
        return AjaxResult.success();
    }

    @PostMapping("/sendImgMsg")
    public AjaxResult sendImgMsg(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.sendImgMsg(wechatMsg);
        return AjaxResult.success();
    }

    @PostMapping("/sendATMsg")
    public AjaxResult sendATMsg(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.sendATMsg(wechatMsg);
        return AjaxResult.success();
    }

    @PostMapping("/sendAnnex")
    public AjaxResult sendAnnex(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.sendAnnex(wechatMsg);
        return AjaxResult.success();
    }


    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 获取信息 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    @GetMapping("/getWeChatUserList")
    public String getWeChatUserList() {

        //计时
        long start = System.currentTimeMillis();
        //保证多个请求到来时候，一个时刻只有一个请求在等待
        MsgVO.getLock().lock();
        MsgVO.setAllList("tbd1");
        MsgVO.setHasGetAllList(false);
        //发送websocket请求获取列表
        wechatBotService.getWeChatUserList();
        while (true) {
            //等待结果
            if (MsgVO.isHasGetAllList()) {
                if (!"tbd1".equals(MsgVO.getAllList())) {
                    //等到数据以后解锁，返回数据,用try-finally保证返回的数据准备好以后再释放锁，避免多线程下出现数据冲突
                    try {
                        return MsgVO.getAllList();
                    } finally {
                        MsgVO.getLock().unlock();
                    }
                }
            }
            //如果超过10秒，可能是websocket不服务了或者怎么样，那就返回500，不能一直阻塞
            if (System.currentTimeMillis() - start > 10000L) {
                //这时候可以直接释放掉，返回了
                MsgVO.getLock().unlock();
                return "500" + "：getWeChatUserList error";
            }
        }

    }

    @GetMapping("/getChatroomMemberNick/{roomid}/{wxid}")
    public String getChatroomMemberNick(@PathVariable("roomid") String roomid, @PathVariable("wxid") String wxid) {
        //计时
        long start = System.currentTimeMillis();
        //保证多个请求到来时候，一个时刻只有一个请求在等待
        //后续如果点开一个群，展示所有成员昵称的话，加锁保证websocket获取想要的数据的方式还需要改善
        //其实也可以增量调用这个接口，然后群信息放到数据库中，定时更新就好了，保证CAP中的AP
        MsgVO.getLock().lock();
        MsgVO.setMemDetail("tbd2");
        MsgVO.setHasGetMemDetail(false);
        //发送websocket请求获取列表
        wechatBotService.getChatroomMemberNick(roomid, wxid);
        while (true) {
            //等待结果
            if (MsgVO.isHasGetMemDetail()) {
                if (!"tbd2".equals(MsgVO.getMemDetail())) {
                    //等到数据以后解锁，返回数据,用try-finally保证返回的数据准备好以后再释放锁，避免多线程下出现数据冲突
                    try {
                        return MsgVO.getMemDetail();
                    } finally {
                        MsgVO.getLock().unlock();
                    }
                }
            }
            //如果超过10秒，可能是websocket不服务了或者怎么样，那就返回500，不能一直阻塞
            if (System.currentTimeMillis() - start > 10000L) {
                //这时候可以直接释放掉，返回了
                MsgVO.getLock().unlock();
                return "500" + "：getChatroomMemberNick/{roomid: " + roomid + "}/{wxid: " + roomid + "} error";
            }
        }
    }

    @GetMapping("/getMemberId")
    public String getMemberId() {
        //计时
        long start = System.currentTimeMillis();
        //保证多个请求到来时候，一个时刻只有一个请求在等待
        MsgVO.getLock().lock();
        MsgVO.setRoomListWithMember("tbd3");
        MsgVO.setHasGetRoomListWithMember(false);
        //发送websocket请求获取列表
        wechatBotService.getMemberId();
        while (true) {
            //等待结果
            if (MsgVO.isHasGetRoomListWithMember()) {
                if (!"tbd3".equals(MsgVO.getRoomListWithMember())) {
                    //等到数据以后解锁，返回数据,用try-finally保证返回的数据准备好以后再释放锁，避免多线程下出现数据冲突
                    try {
                        return MsgVO.getRoomListWithMember();
                    } finally {
                        MsgVO.getLock().unlock();
                    }
                }
            }
            //如果超过10秒，可能是websocket不服务了或者怎么样，那就返回500，不能一直阻塞
            if (System.currentTimeMillis() - start > 10000) {
                //这时候可以直接释放掉，返回了
                MsgVO.getLock().unlock();
                return "500" + "：getMemberId error";
            }
        }
    }

}
