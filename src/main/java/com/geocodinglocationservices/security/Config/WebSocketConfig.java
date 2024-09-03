package com.geocodinglocationservices.security.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker("/topic","/queue");
        registry.setApplicationDestinationPrefixes("/app");


    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
//        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000")
//                .setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000")
                .setAllowedOriginPatterns("*");
//                .setStreamBytesLimit(512 * 1024)
//                .setHttpMessageCacheSize(1000)
//                .setDisconnectDelay(30 * 1000);



    }



}
