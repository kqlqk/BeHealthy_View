package me.kqlqk.behealthy.view.feign_client;


import feign.Param;
import me.kqlqk.behealthy.view.dto.auth.LoginDTO;
import me.kqlqk.behealthy.view.dto.auth.RegistrationDTO;
import me.kqlqk.behealthy.view.dto.condition.UserConditionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "Gateway")
public interface GatewayClient {

    @PostMapping("/api/v1/login")
    ResponseEntity<?> logIn(@Param LoginDTO loginDTO);

    @PostMapping("/api/v1/registration")
    ResponseEntity<?> registration(@Param RegistrationDTO registrationDTO);

    @GetMapping("/api/v1/users/{id}/condition")
    UserConditionDTO getUserCondition(@PathVariable long id,
                                      @RequestHeader("Authorization_access") String accessToken,
                                      @RequestHeader("Authorization_refresh") String refreshToken);

    @PostMapping("/api/v1/users/{id}/condition")
    void createUserCondition(@PathVariable long id,
                             @RequestBody UserConditionDTO userConditionDTO,
                             @RequestHeader("Authorization_access") String accessToken,
                             @RequestHeader("Authorization_refresh") String refreshToken);
}
