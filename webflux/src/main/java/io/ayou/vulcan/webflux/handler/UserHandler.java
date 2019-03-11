package io.ayou.vulcan.webflux.handler;

import io.ayou.vulcan.webflux.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author AYOU
 * @ClassName UserHandler
 * WebClient为异步非阻塞http
 * RestTemplate为阻塞http
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
        get();
        String id = request.pathVariable("id");
        WebClient client = WebClient.create(baseUrl);
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
        userMono.subscribe(System.err::println);
        return ServerResponse.ok().body(userMono, User.class);
    }

    public Mono<ServerResponse> getByRestTemplate(ServerRequest request) {
        String id = request.pathVariable("id");
        new OkHttp3ClientHttpRequestFactory();
        //new Netty4ClientHttpRequestFactory()
        new ReactorClientHttpConnector();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(factory);
        Mono<User> userMono = Mono.just(restTemplate.getForObject("/api/user/" + id, User.class));
        return ServerResponse.ok().body(userMono, User.class);
    }

    public void get() {
        WebClient client = WebClient.create("http://www.baidu.com");
        for (int i = 1; i < 100; i++) {
            Mono<String> stringMono = client.get().retrieve().bodyToMono(String.class);
            stringMono.subscribe(str -> System.out.println(str));
        }
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        //jsessionid=k2Jclb2QTVvTqaJB5g7P4CPCB72kQAKv2k1L5nr-kLVgfz1vIsuy!1228397469"
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "ex_lidan002");
        formData.add("password", "123456");
        formData.add("lt", "LT-1301-FPPAOgw0HOK1Tree0lHOPnM7uOl9bW-CAS.01");
        formData.add("execution", "e1s1");
        formData.add("_eventId", "submit");
        //WebClient client = WebClient.create();

        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .defaultHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
                .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,en-CA;q=0.6")
                .defaultHeader(HttpHeaders.CONNECTION, "keep-alive")
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36")
                .build();
        Mono<String> stringMono = client.post()
                //http://10.196.120.4:8080/ipartner/index
                .uri("http://10.196.20.4:7001/cas/login?service=http%3A%2F%2F10.196.120.4%3A8080%2Fipartner%2Fj_spring_cas_security_check")
                //.accept(MediaType.APPLICATION_JSON)
                .cookie("JSESSIONID", "DDtqk8VKV2P04td9SQ68d3aAUZNrtwYe5wsDy-O99jq8q4JRKQzX!-1629022944")
                .header(HttpHeaders.HOST, "10.196.20.4:7001")
                .header(HttpHeaders.ORIGIN, "Origin: http://10.196.20.4:7001")
                .header(HttpHeaders.REFERER, "http://10.196.20.4:7001/cas/login?service=http%3A%2F%2F10.196.120.4%3A8080%2Fipartner%2Fj_spring_cas_security_check")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class);
        //System.out.println(stringMono.block());

        return ServerResponse.ok().body(stringMono, String.class);
    }

    public Mono<ServerResponse> p(ServerRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "ex_lidan002");
        formData.add("password", "123456");
        formData.add("lt", "LT-605-OcCpr0brGVcI7jkElXlMQT4Tbh3bk1-CAS.01");
        formData.add("execution", "e1s1");
        formData.add("_eventId", "submit");

        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .defaultHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
                .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,en-CA;q=0.6")
                .defaultHeader(HttpHeaders.CONNECTION, "keep-alive")
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36")
                .build();

        Mono<String> stringMono = client.get()
                .uri("http://10.196.120.4:8080/ipartner/index")
                //.accept(MediaType.APPLICATION_JSON)
                .cookie("serverInfo", "10.196.120.4:8080")
                .cookie("JSESSIONID", "C3FFFEEDD0EBE9D45BC1FB2C82873F16")
                .header(HttpHeaders.HOST, "10.196.120.4:8080")
                .retrieve()
                .bodyToMono(String.class);
        return ServerResponse.ok().body(Mono.just(stringMono), Object.class);
    }
}
