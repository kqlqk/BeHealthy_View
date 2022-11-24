package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.auth.LoginDTO;
import me.kqlqk.behealthy.view.dto.auth.RegistrationDTO;
import me.kqlqk.behealthy.view.dto.auth.UserAuthDTO;
import me.kqlqk.behealthy.view.feign_client.AuthenticationClient;
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
    private final AuthenticationClient authenticationClient;

    @Autowired
    public GuestController(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
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
    public String logIn(@ModelAttribute("loginDTO") @Valid LoginDTO loginDTO,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "guest/LoginPage";
        }

        UserAuthDTO userAuthDTO = authenticationClient.getUserByEmail(loginDTO.getEmail());

        return "redirect:/me/" + userAuthDTO.getId();
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());

        return "guest/RegistrationPage";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors() || !registrationDTO.getConfirmPassword().equals(registrationDTO.getPassword())) {
            return "guest/RegistrationPage";
        }

        UserAuthDTO userAuthDTO = authenticationClient.getUserByEmail(registrationDTO.getEmail());

        return "redirect:/me/" + userAuthDTO.getId();
    }

}
