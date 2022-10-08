package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.LoginDTO;
import me.kqlqk.behealthy.view.feign_client.GatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class GuestController {
    private final GatewayClient gatewayClient;

    @Autowired
    public GuestController(GatewayClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }

    @GetMapping
    public String getMainPage() {
        return "guest/MainPage";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());

        return "guest/LoginPage";
    }

    @PostMapping("/login")
    public String logIn(@ModelAttribute("loginDTO") @Valid LoginDTO loginDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "guest/LoginPage";
        }

        try {
            gatewayClient.logIn(loginDTO);
        } catch (RuntimeException e) {
            model.addAttribute("exception", e.getMessage());
            return "guest/LoginPage";
        }

        return "redirect:/me";
    }

}
