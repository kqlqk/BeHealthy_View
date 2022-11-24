package me.kqlqk.behealthy.view.feign_client;

import me.kqlqk.behealthy.view.dto.auth.UserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authenticationService")
public interface AuthenticationClient {
    @GetMapping("/api/v1/users")
    UserAuthDTO getUserByEmail(@RequestParam String email);
}
