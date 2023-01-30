package com.xzy.wechatbot.util;

import cn.hutool.http.HttpRequest;

/**
 * @description: HttpClient
 * @author: Xie zy
 * @create: 2023.01.30
 */
public class HttpClient {

    public static String doPost(String url, String body) {
        return HttpRequest.post(url)
                .header("Content-Type", "application/json")//头信息，多个头信息多次调用此方法即可
                .body(body)
                .execute().body();
    }

}
