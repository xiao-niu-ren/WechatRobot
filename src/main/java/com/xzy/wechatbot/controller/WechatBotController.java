package com.xzy.wechatbot.controller;

import com.xzy.wechatbot.common.util.AjaxResult;
import com.xzy.wechatbot.domain.WechatMsg;
import com.xzy.wechatbot.service.WechatBotService;
import com.xzy.wechatbot.vo.MsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 获取的方法：/getWeChatUserList                         获取所有联系人列表，包括微信好友、群组、公众号
 * /getMemberId                               获取所有群组列表及该群组所有群成员id
 * /getChatroomMemberNick/{roomid}/{wxid}     根据群组id和该组某成员id查询该成员群昵称
 * <p>
 * 发送的方法：/sendTextMsg                               发送文本消息
 * /sendImgMsg                                发送图片消息
 * /sendATMsg                                 发送@消息
 * /sendAnnex                                 发送附件消息
 */
@RestController
public class WechatBotController {

    //互斥锁
    private static Lock lock = new ReentrantLock();

    @Autowired
    private WechatBotService wechatBotService;


    /**
     * 描述: 通用请求接口
     *
     * @param wechatMsg
     * @return com.xzy.wechatbot.common.util.AjaxResult
     */
    @PostMapping("/wechatCommon")
    public AjaxResult wechatCommon(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.wechatCommon(wechatMsg);
        return AjaxResult.success();
    }


    /**
     * 描述: 发送文本消息
     *
     * @param wechatMsg
     * @return com.xzy.wechatbot.common.util.AjaxResult
     */
    @PostMapping("/sendTextMsg")
    public AjaxResult sendTextMsg(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.sendTextMsg(wechatMsg);
        return AjaxResult.success();
    }

    /**
     * 描述: 发送图片消息
     *
     * @param wechatMsg
     * @return com.xzy.wechatbot.common.util.AjaxResult
     */
    @PostMapping("/sendImgMsg")
    public AjaxResult sendImgMsg(@RequestBody WechatMsg wechatMsg) {
        // 发送消息
        wechatBotService.sendImgMsg(wechatMsg);
        return AjaxResult.success();
    }

    /**
     * 描述: 群组内发送@指定人消息(dll 3.1.0.66版本不可用)
     *
     * @param wechatMsg
     * @return com.xzy.wechatbot.common.util.AjaxResult
     */
    @PostMapping("/sendATMsg")
    public AjaxResult sendATMsg(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.sendATMsg(wechatMsg);
        return AjaxResult.success();
    }

    /**
     * 描述: 发送附件
     *
     * @param wechatMsg
     * @return com.xzy.wechatbot.common.util.AjaxResult、
     */
    @PostMapping("/sendAnnex")
    public AjaxResult sendAnnex(@RequestBody WechatMsg wechatMsg) {
        wechatBotService.sendAnnex(wechatMsg);
        return AjaxResult.success();
    }


    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 获取信息 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 描述: 获取微信群组,联系人列表
     *
     * @param
     * @return com.xzy.wechatbot.common.util.AjaxResult
     */
    @GetMapping("/getWeChatUserList")
    public String getWeChatUserList() {

        //计时
        long start = System.currentTimeMillis();
        //保证多个请求到来时候，一个时刻只有一个请求在等待
        lock.lock();
        MsgVO.setAllList("tbd1");
        MsgVO.setHasGetAllList(false);
        //发送websocket请求获取列表
        wechatBotService.getWeChatUserList();
        while (true) {
            //等待结果
            if (MsgVO.isHasGetAllList()) {
                if (!MsgVO.getAllList().equals("tbd1")) {
                    //等到数据以后解锁，返回数据,用try-finally保证返回的数据准备好以后再释放锁，避免多线程下出现数据冲突
                    try {
                        return MsgVO.getAllList();
                    } finally {
                        lock.unlock();
                    }
                }
            }
            //如果超过10秒，可能是websocket不服务了或者怎么样，那就返回500，不能一直阻塞
            if (System.currentTimeMillis() - start >= 10000) {
                //这时候可以直接释放掉，返回了
                lock.unlock();
                return "500" + "：getWeChatUserList失败";
            }
        }

//        return AjaxResult.success("执行结果稍后会打印在控制台");
    }

    /**
     * 描述: 获取群组里指定联系人的详细信息
     *
     * @param roomid 群组id
     * @param wxid   指定用户id
     * @return com.xzy.wechatbot.common.util.AjaxResult
     */
    @GetMapping("/getChatroomMemberNick/{roomid}/{wxid}")
    public String getChatroomMemberNick(@PathVariable("roomid") String roomid, @PathVariable("wxid") String wxid) {
        //计时
        long start = System.currentTimeMillis();
        //保证多个请求到来时候，一个时刻只有一个请求在等待
        //后续如果点开一个群，展示所有成员昵称的话，加锁保证websocket获取想要的数据的方式还需要改善
        //其实也可以增量调用这个接口，然后群信息放到数据库中，定时更新就好了，保证CAP中的AP
        lock.lock();
        MsgVO.setMemDetail("tbd2");
        MsgVO.setHasGetMemDetail(false);
        //发送websocket请求获取列表
        wechatBotService.getChatroomMemberNick(roomid, wxid);
        while (true) {
            //等待结果
            if (MsgVO.isHasGetMemDetail()) {
                if (!MsgVO.getMemDetail().equals("tbd2")) {
                    //等到数据以后解锁，返回数据,用try-finally保证返回的数据准备好以后再释放锁，避免多线程下出现数据冲突
                    try {
                        return MsgVO.getMemDetail();
                    } finally {
                        lock.unlock();
                    }
                }
            }
            //如果超过10秒，可能是websocket不服务了或者怎么样，那就返回500，不能一直阻塞
            if (System.currentTimeMillis() - start >= 10000) {
                //这时候可以直接释放掉，返回了
                lock.unlock();
                return "500" + "：getChatroomMemberNick/{roomid}/{wxid}失败";
            }
        }
    }

    /**
     * 描述: 获取所有群组以及成员
     *
     * @return com.xzy.wechatbot.common.util.AjaxResult
     */
    @GetMapping("/getMemberId")
    public String getMemberId() {
        //计时
        long start = System.currentTimeMillis();
        //保证多个请求到来时候，一个时刻只有一个请求在等待
        lock.lock();
        MsgVO.setRoomListWithMember("tbd3");
        MsgVO.setHasGetRoomListWithMember(false);
        //发送websocket请求获取列表
        wechatBotService.getMemberId();
        while (true) {
            //等待结果
            if (MsgVO.isHasGetRoomListWithMember()) {
                if (!MsgVO.getRoomListWithMember().equals("tbd3")) {
                    //等到数据以后解锁，返回数据,用try-finally保证返回的数据准备好以后再释放锁，避免多线程下出现数据冲突
                    try {
                        return MsgVO.getRoomListWithMember();
                    } finally {
                        lock.unlock();
                    }
                }
            }
            //如果超过10秒，可能是websocket不服务了或者怎么样，那就返回500，不能一直阻塞
            if (System.currentTimeMillis() - start >= 10000) {
                //这时候可以直接释放掉，返回了
                lock.unlock();
                return "500" + "：/getMemberId失败";
            }
        }
    }

//    /**
//     * 描述: 获取个人详细信息 3.2.2.121版本dll 未提供该接口
//     *
//     * @param
//     * @return com.xzy.wechatbot.common.util.AjaxResult
//     */
//    // @GetMapping("/getPersonalDetail/{wxid}")
//    public AjaxResult getPersonalDetail(@PathVariable("wxid") String wxid) {
//        wechatBotService.getPersonalDetail(wxid);
//        return AjaxResult.success();
//    }
}
