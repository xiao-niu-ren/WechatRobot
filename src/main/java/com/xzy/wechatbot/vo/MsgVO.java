package com.xzy.wechatbot.vo;

public class MsgVO {
    //好友、群聊、公众号 列表
    public static String allList;
    //群成员带成员id信息
    public static String roomListWithMember;
    //群成员昵称
    public static String memDetail;

    public static boolean hasGetAllList;
    public static boolean hasGetRoomListWithMember;
    public static boolean hasGetMemDetail;

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
