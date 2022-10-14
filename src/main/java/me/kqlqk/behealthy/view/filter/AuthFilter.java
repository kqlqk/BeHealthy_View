package me.kqlqk.behealthy.view.filter;

import lombok.NonNull;
import me.kqlqk.behealthy.view.dto.auth.UserAuthDTO;
import me.kqlqk.behealthy.view.exception.exceptions.TokenException;
import me.kqlqk.behealthy.view.feign_client.AuthenticationClient;
import me.kqlqk.behealthy.view.model.AuthInfo;
import me.kqlqk.behealthy.view.service.AuthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(2)
public class AuthFilter extends OncePerRequestFilter {
    private final AuthenticationClient authenticationClient;
    private final AuthInfoService authInfoService;
    private final String[] urisNotToCheck = {
            "/",
            "/login",
            "/registration"
    };

    @Autowired
    public AuthFilter(AuthenticationClient authenticationClient, AuthInfoService authInfoService) {
        this.authenticationClient = authenticationClient;
        this.authInfoService = authInfoService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> tokens;

        for (String uri : urisNotToCheck) {
            if (request.getRequestURI().equals(uri)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            tokens = getTokensFromHeaders(request);
        } catch (TokenException e) {
            response.sendRedirect("/login");
            return;
        }
        String access = tokens.get("access");
        String refresh = tokens.get("refresh");

        if (!authenticationClient.validateRefreshTokenFromRequest("Bearer_" + refresh).isValid()) {
            response.sendRedirect("/login");
            return;
        }

        if (!authenticationClient.validateAccessTokenFromRequest("Bearer_" + access).isValid()) {
            if (!authenticationClient.validateRefreshTokenFromRequest("Bearer_" + refresh).isValid()) {
                response.sendRedirect("/login");
                return;
            }

            String userEmail = authenticationClient.getEmailFromRefreshToken("Bearer_" + refresh).get("email");
            UserAuthDTO userAuthDTO = authenticationClient.getUserByEmail(userEmail);
            tokens = authenticationClient.updateTokensForUser(userAuthDTO.getId());

            AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());
            authInfo.setAccessToken("Bearer_" + tokens.get("access"));
            authInfo.setRefreshToken("Bearer_" + tokens.get("refresh"));
            authInfoService.saveOrUpdate(authInfo);
        }


        String email = authenticationClient.getEmailFromRefreshToken("Bearer_" + tokens.get("refresh")).get("email");
        UserAuthDTO userAuthDTO = authenticationClient.getUserByEmail(email);

        AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());
        authInfo.setUserId(userAuthDTO.getId());
        authInfoService.saveOrUpdate(authInfo);

        filterChain.doFilter(request, response);
    }

    private Map<String, String> getTokensFromHeaders(@NonNull HttpServletRequest request) {
        String bearerWithAccessToken = request.getHeader("Authorization_access");
        String bearerWithRefreshToken = request.getHeader("Authorization_refresh");

        if (bearerWithAccessToken == null) {
            throw new TokenException("Authorization_access header not found");
        }
        if (!bearerWithAccessToken.startsWith("Bearer_")) {
            throw new TokenException("Access token should starts with Bearer_");
        }

        if (bearerWithRefreshToken == null) {
            throw new TokenException("Authorization_refresh header not found");
        }
        if (!bearerWithRefreshToken.startsWith("Bearer_")) {
            throw new TokenException("Refresh token should starts with Bearer_");
        }
        Map<String, String> res = new HashMap<>();
        res.put("access", bearerWithAccessToken.substring(7));
        res.put("refresh", bearerWithRefreshToken.substring(7));
        return res;
    }

}
