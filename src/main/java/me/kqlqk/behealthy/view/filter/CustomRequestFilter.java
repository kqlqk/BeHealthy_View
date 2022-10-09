package me.kqlqk.behealthy.view.filter;

import lombok.NonNull;
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

@Component
@Order(1)
public class CustomRequestFilter extends OncePerRequestFilter {
    private final AuthInfoService authInfoService;
    private final String[] urisNotToCheck = {
            "/",
            "/login",
            "/registration"
    };

    @Autowired
    public CustomRequestFilter(AuthInfoService authInfoService) {
        this.authInfoService = authInfoService;
    }


    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws IOException, ServletException {
        for (String uri : urisNotToCheck) {
            if (request.getRequestURI().equals(uri)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        HttpServletRequestWithCustomHeaders customRequest = new HttpServletRequestWithCustomHeaders(request);

        AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());

        if (authInfo != null) {
            customRequest.addHeader("Authorization_access", authInfo.getAccessToken());
            customRequest.addHeader("Authorization_refresh", authInfo.getRefreshToken());
        } else {
            response.sendRedirect("/login");
            return;
        }

        filterChain.doFilter(customRequest, response);
    }

}
