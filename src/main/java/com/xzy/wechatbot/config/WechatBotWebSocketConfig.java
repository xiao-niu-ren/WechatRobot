package com.xzy.wechatbot.config;

import com.xzy.wechatbot.client.WechatBotClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
public class WechatBotWebSocketConfig {

    /** 微信bot 连接地址 */
    @Value("${wechatBot.url}")
    private String wechatBotUrl;

    /** 注入WebSocket Client */
    @Bean
    public WechatBotClient initWechatBotClient() throws URISyntaxException {
        WechatBotClient botClient = new WechatBotClient(wechatBotUrl);
        botClient.connect();
        return botClient;
    }

}
