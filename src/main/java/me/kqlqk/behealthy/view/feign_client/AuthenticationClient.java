package me.kqlqk.behealthy.view.feign_client;

import me.kqlqk.behealthy.view.dto.ValidateDTO;
import me.kqlqk.behealthy.view.dto.auth.UserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "AuthenticationService")
public interface AuthenticationClient {

    @GetMapping("/api/v1/auth/validate_access_token")
    ValidateDTO validateAccessTokenFromRequest(@RequestHeader("Authorization_access") String accessToken);

    @GetMapping("/api/v1//auth/validate_refresh_token")
    ValidateDTO validateRefreshTokenFromRequest(@RequestHeader("Authorization_refresh") String refreshToken);

    @GetMapping("/api/v1/auth/get_email_from_refresh_token")
    Map<String, String> getEmailFromRefreshToken(@RequestHeader("Authorization_refresh") String refreshToken);

    @GetMapping("/api/v1/users/{id}/update_tokens")
    Map<String, String> updateTokensForUser(@PathVariable long id);

    @GetMapping("/api/v1/users")
    UserAuthDTO getUserByEmail(@RequestParam String email);
}