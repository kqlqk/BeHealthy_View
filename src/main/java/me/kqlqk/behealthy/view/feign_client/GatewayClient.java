package me.kqlqk.behealthy.view.feign_client;


import feign.Param;
import me.kqlqk.behealthy.view.dto.LoginDTO;
import me.kqlqk.behealthy.view.dto.RegistrationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "Gateway")
public interface GatewayClient {

    @PostMapping("/api/v1/login")
    void logIn(@Param LoginDTO loginDTO);

    @PostMapping("/api/v1/registration")
    void registration(@Param RegistrationDTO registrationDTO);
}
