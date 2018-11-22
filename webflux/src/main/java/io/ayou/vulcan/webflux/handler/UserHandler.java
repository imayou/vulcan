package io.ayou.vulcan.webflux.handler;

import io.ayou.vulcan.webflux.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author AYOU
 * @ClassName UserHandler
 */
@Component
public class UserHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * domain
     */
    @Autowired
    private User user;

    String baseUrl = "http://localhost:9090";
    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);

    public Mono<ServerResponse> list(ServerRequest request) {
        Flux<User> userFlux = user.list();
        //会触发2次请求，会导致流关闭
        ///userFlux.subscribe(user1 -> logger.info(user1.toString()));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(userFlux, User.class);
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        String id = request.pathVariable("id");
        Mono<User> userMono = user.get(id);
        userMono.subscribe(user1 -> logger.info(user1.toString()));
        return userMono.flatMap(user -> ServerResponse.ok().body(userMono, User.class)).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getByWebClient(ServerRequest request) {
        String id = request.pathVariable("id");
        WebClient client = WebClient.create("http://127.0.0.1:9090");
        Mono<User> userMono = client.get()
                .uri("/api/user/{id}", id).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class);
        return ServerResponse.ok().body(userMono, User.class);
    }

    public Mono<ServerResponse> getByWebClient2(ServerRequest request) {
        String id = request.pathVariable("id");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES);
        WebClient webClient = WebClient.builder().uriBuilderFactory(factory).build();
        WebClient.RequestHeadersUriSpec d = webClient.get();
        d.uri("/api/user/{id}", id);
        WebClient.ResponseSpec rsp = d.retrieve();
        Mono<User> userMono = rsp.bodyToMono(User.class);
        //会触发2次请求，会导致流关闭
        ///userMono.subscribe(user -> System.err.println(user));
        return ServerResponse.ok().body(userMono, User.class);
    }

    public Mono<ServerResponse> getByRestTemplate(ServerRequest request) {
        String id = request.pathVariable("id");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(factory);
        Mono<User> userMono = Mono.just(restTemplate.getForObject("/api/user/" + id, User.class));
        return ServerResponse.ok().body(userMono, User.class);
    }
}
