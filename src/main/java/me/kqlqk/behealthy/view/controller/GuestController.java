package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GuestController {

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
    public String logIn(@ModelAttribute("loginDTO") LoginDTO loginDTO) {
        System.out.println(loginDTO.getEmail() + ":" + loginDTO.getPassword());
        return "redirect:/me";
    }

}
