package io.ayou.vulcan.webflux.domain;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author AYOU
 * @ClassName User
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class User {
    private String id;
    private String name;
    private String email;
    private String address;
    private String uuid;
    private Integer age;

    public User() {
    }

    public Mono<User> get(String id) {
        if ("0".equals(id)) {
            return Mono.just(null);
        }
        return Mono.just(new User(id, "ayou", "550244300@qq.com", "云南", UUID.randomUUID().toString(), 18));
    }

    public Flux<User> list() {
        List<User> users = Arrays.asList(
                new User("1", "ayou", "550244300@qq.com", "云南", UUID.randomUUID().toString(), 18),
                new User("2", "玥儿", "666666@qq.com", "云南", UUID.randomUUID().toString(), 18)
        );
        return Flux.fromStream(users.stream());
    }

    public User(String id, String name, String email, String address, String uuid, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.uuid = uuid;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", uuid='" + uuid + '\'' +
                ", age=" + age +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}





