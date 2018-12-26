package io.ayou.vulcan.webflux.action;

import io.ayou.vulcan.webflux.service.HelloWorldMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @ClassName MessageController
 * @author AYOU
 */
@RestController
public class MessageController {
    private final HelloWorldMessageService messages;

    public MessageController(HelloWorldMessageService messages) {
        this.messages = messages;
    }

    @GetMapping("/message")
    public Mono<String> message() {
        return this.messages.findMessage();
    }
}
