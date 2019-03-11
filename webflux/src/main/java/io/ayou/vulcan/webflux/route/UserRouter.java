package io.ayou.vulcan.webflux.route;

import io.ayou.vulcan.webflux.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author AYOU
 * @ClassName UserR
 */
@Configuration
public class UserRouter {

    @Bean
    @Autowired
    public RouterFunction<ServerResponse> responseRouterFunction(UserHandler userHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/api/users"), userHandler::list)
                .andRoute(RequestPredicates.GET("/api/user/{id}"), userHandler::get)
                .andRoute(RequestPredicates.GET("/user/login"), userHandler::login)
                .andRoute(RequestPredicates.GET("/user/login2"), userHandler::p)
                .andRoute(RequestPredicates.GET("/api/user/{id}/web1"), userHandler::getByWebClient)
                .andRoute(RequestPredicates.GET("/api/user/{id}/web2"), userHandler::getByWebClient2)
                .andRoute(RequestPredicates.GET("/api/user/{id}/rest"), userHandler::getByRestTemplate);
    }

}
