package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.condition.UserConditionDTO;
import me.kqlqk.behealthy.view.feign_client.GatewayClient;
import me.kqlqk.behealthy.view.model.AuthInfo;
import me.kqlqk.behealthy.view.service.AuthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
        model.addAttribute("userId", id);
        AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());

        if (id != authInfo.getUserId()) {
            return "redirect:/login";
        }

        try {
            UserConditionDTO userConditionDTO = gatewayClient.getUserCondition(id, authInfo.getAccessToken(), authInfo.getRefreshToken());
            model.addAttribute("setCondition", true);
            model.addAttribute("condition", userConditionDTO);

        } catch (RuntimeException e) {
            model.addAttribute("condition", new UserConditionDTO());
            model.addAttribute("setCondition", false);
        }

        return "user/MePage";
    }

    @PostMapping("/me/{id}")
    public String setCondition(@PathVariable long id,
                               @ModelAttribute("condition") @Valid UserConditionDTO userConditionDTO,
                               BindingResult bindingResult,
                               Model model,
                               HttpServletRequest request) {
        AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());

        if (id != authInfo.getUserId()) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("setCondition", false);
            model.addAttribute("userId", id);
            return "user/MePage";
        }

        try {
            gatewayClient.createUserCondition(id, userConditionDTO, authInfo.getAccessToken(), authInfo.getRefreshToken());
        } catch (RuntimeException e) {
            return "redirect:/me/" + id;
        }

        return "redirect:/me/" + id;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        authInfoService.deleteByRemoteAddr(request.getRemoteAddr());

        return "redirect:/";
    }
}
