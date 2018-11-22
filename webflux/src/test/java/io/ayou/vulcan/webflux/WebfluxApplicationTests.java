package io.ayou.vulcan.webflux;

import io.ayou.vulcan.webflux.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

/**
 * https://github.com/spring-projects/spring-framework/blob/master/spring-webflux/src/test/java/org/springframework/web/reactive/function/client/WebClientIntegrationTests.java
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WebfluxApplicationTests {

    private WebClient webClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    String baseUrl = "http://localhost:9090";
    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);

    @Before
    public void init() {
        this.webClient = WebClient.builder().uriBuilderFactory(factory).build();
    }

    @Test
    public void contextLoads() {
        WebClient.RequestHeadersUriSpec d = webClient.get();
        d.uri("/api/user/{id}", 1);
        WebClient.ResponseSpec rsp = d.retrieve();
        Mono<User> userMono = rsp.bodyToMono(User.class);
        userMono.subscribe(user -> logger.error(user.getId().equals("1") + ""));
    }

}
