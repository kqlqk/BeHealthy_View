package me.kqlqk.behealthy.view.feign_client;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "Gateway")
public interface GatewayClient {
}
