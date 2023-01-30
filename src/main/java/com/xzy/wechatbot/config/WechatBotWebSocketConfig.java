package com.xzy.wechatbot.config;

import com.xzy.wechatbot.client.WechatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
public class WechatBotWebSocketConfig {

    /** 微信bot 连接地址 */
    @Value("${wechatBot.wechatUrl}")
    private String wechatBotUrl;

    /** WechatMessage 连接地址 */
    @Value("${wechatBot.wechatMessageUrl}")
    private String wechatMessageUrl;

    /** 注入WebSocket Client */
    @Bean
    public WechatClient initWechatBotClient() throws URISyntaxException {
        WechatClient botClient = new WechatClient(wechatBotUrl);
        WechatClient.setWechatMessageUrl(wechatMessageUrl);
        botClient.connect();
        return botClient;
    }

}
