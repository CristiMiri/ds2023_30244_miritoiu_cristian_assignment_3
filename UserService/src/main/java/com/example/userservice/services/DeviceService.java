package com.example.userservice.services;

import com.example.userservice.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DeviceService {
    private final WebClient webClient;
    private final String baseUrl="http://localhost:8081";
//    private  final String baseUrl = "http://device-service:8081";


    public DeviceService() {
        webClient = WebClient.create(baseUrl);
    }

    public Mono<String> registerDeviceUser(User user) {
        return webClient.post()
                .uri("/device_users/register")
                .body(Mono.just(user), User.class)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        Mono.error(new RuntimeException("Failed to register device user: Status code " + response.statusCode()))
                )
                .bodyToMono(String.class);
    }

    public Mono<String> updateDeviceUser(User user) {
        return webClient.put()
                .uri("/device_users/update")
                .body(Mono.just(user), User.class)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        Mono.error(new RuntimeException("Failed to update device user: Status code " + response.statusCode()))
                )
                .bodyToMono(Void.class)
                .map(Void -> "Device User Updated");
    }

    public Mono<String> deleteDeviceUser(User user) {
        return webClient.delete()
                .uri("/device_users/delete/{id}", user.getId())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        Mono.error(new RuntimeException("Failed to delete device user: Status code " + response.statusCode()))
                )
                .bodyToMono(Void.class)
                .map(Void -> "Device User Deleted");
    }
}
