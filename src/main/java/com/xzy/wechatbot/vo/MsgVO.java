package com.xzy.wechatbot.vo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MsgVO {

    //互斥锁
    private static Lock lock = new ReentrantLock();

    //好友、群聊、公众号 列表
    private static String allList;
    private static boolean hasGetAllList;
    //群成员带成员id信息
    private static String roomListWithMember;
    private static boolean hasGetRoomListWithMember;
    //群成员昵称
    private static String memDetail;
    private static boolean hasGetMemDetail;

    public static Lock getLock() {
        return lock;
    }

    public static String getAllList() {
        return allList;
    }

    public static void setAllList(String allList) {
        MsgVO.allList = allList;
    }

    public static String getRoomListWithMember() {
        return roomListWithMember;
    }

    public static void setRoomListWithMember(String roomListWithMember) {
        MsgVO.roomListWithMember = roomListWithMember;
    }

    public static String getMemDetail() {
        return memDetail;
    }

    public static void setMemDetail(String memDetail) {
        MsgVO.memDetail = memDetail;
    }

    public static boolean isHasGetAllList() {
        return hasGetAllList;
    }

    public static void setHasGetAllList(boolean hasGetAllList) {
        MsgVO.hasGetAllList = hasGetAllList;
    }

    public static boolean isHasGetRoomListWithMember() {
        return hasGetRoomListWithMember;
    }

    public static void setHasGetRoomListWithMember(boolean hasGetRoomListWithMember) {
        MsgVO.hasGetRoomListWithMember = hasGetRoomListWithMember;
    }

    public static boolean isHasGetMemDetail() {
        return hasGetMemDetail;
    }

    public static void setHasGetMemDetail(boolean hasGetMemDetail) {
        MsgVO.hasGetMemDetail = hasGetMemDetail;
    }

}
