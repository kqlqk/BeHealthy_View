package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.condition.UserConditionDTO;
import me.kqlqk.behealthy.view.feign_client.GatewayClient;
import me.kqlqk.behealthy.view.model.AuthInfo;
import me.kqlqk.behealthy.view.service.AuthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    private final GatewayClient gatewayClient;
    private final AuthInfoService authInfoService;

    @Autowired
    public HomeController(GatewayClient gatewayClient, AuthInfoService authInfoService) {
        this.gatewayClient = gatewayClient;
        this.authInfoService = authInfoService;
    }

    @GetMapping("/me/{id}")
    public String getCurrentUserPage(@PathVariable long id, Model model, HttpServletRequest request) {
        AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());

        if (id != authInfo.getUserId()) {
            return "redirect:/login";
        }

        try {
            UserConditionDTO userConditionDTO = gatewayClient.getUserCondition(id, authInfo.getAccessToken(), authInfo.getRefreshToken());
            model.addAttribute("setCondition", true);
        } catch (RuntimeException e) {
            model.addAttribute("setCondition", false);
        }

        return "user/MePage";
    }
}
