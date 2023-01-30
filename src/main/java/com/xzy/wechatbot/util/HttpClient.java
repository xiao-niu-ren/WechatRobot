package com.xzy.wechatbot.util;

import cn.hutool.http.HttpRequest;

/**
 * @description: HttpClient
 * @author: Xie zy
 * @create: 2023.01.30
 */
public class HttpClient {

    public static String doGet(String url) {
        return HttpRequest.get(url)
                .header("Accept", "application/json")
                .execute().body();
    }

    public static String doPost(String url, String body) {
        return HttpRequest.post(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(body)
                .execute().body();
    }

}
