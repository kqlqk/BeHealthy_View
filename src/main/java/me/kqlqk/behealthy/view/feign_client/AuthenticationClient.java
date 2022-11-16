package me.kqlqk.behealthy.view.feign_client;

import me.kqlqk.behealthy.view.dto.ValidateDTO;
import me.kqlqk.behealthy.view.dto.auth.UserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "authenticationService")
public interface AuthenticationClient {

    @GetMapping("/api/v1/auth/validate/access")
    ValidateDTO validateAccessTokenFromRequest(@RequestHeader("Authorization_access") String accessToken);

    @GetMapping("/api/v1/auth/validate/refresh")
    ValidateDTO validateRefreshTokenFromRequest(@RequestHeader("Authorization_refresh") String refreshToken);

    @GetMapping("/api/v1/auth/request/refresh/email")
    Map<String, String> getEmailFromRefreshToken(@RequestHeader("Authorization_refresh") String refreshToken);

    @PutMapping("/api/v1/users/{id}/tokens")
    Map<String, String> updateTokensForUser(@PathVariable long id);

    @GetMapping("/api/v1/users")
    UserAuthDTO getUserByEmail(@RequestParam String email);
}
