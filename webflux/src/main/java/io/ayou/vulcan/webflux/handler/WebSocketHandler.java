package io.ayou.vulcan.webflux.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WebSocketHandler
 * @author AYOU
 */
public class WebSocketHandler implements org.springframework.web.reactive.socket.WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        /*Flux<WebSocketMessage> output = session.receive()
                .doOnNext(message -> {
                    System.out.println(message);
                })
                .concatMap(message -> {
                    System.out.println(message);
                })
                .map(value -> session.textMessage("Echo " + value));*/

        Flux<WebSocketMessage> outputx = new Flux<WebSocketMessage>() {
            @Override
            public void subscribe(CoreSubscriber<? super WebSocketMessage> actual) {
                System.out.println("what...fuck!");
            }
        };
        return session.send(outputx);
    }

    @Configuration
    static class WebConfig {
        @Bean
        public HandlerMapping handlerMapping() {
            Map<String, WebSocketHandler> map = new HashMap<>();
            map.put("/socket", new WebSocketHandler());

            SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
            mapping.setUrlMap(map);
            // before annotated controllers
            mapping.setOrder(-1);
            return mapping;
        }

        @Bean
        public WebSocketHandlerAdapter handlerAdapter() {
            return new WebSocketHandlerAdapter();
        }
    }
}

