package me.kqlqk.behealthy.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuestController {

    @GetMapping
    public String getMainPage() {
        return "guest/MainPage";
    }

}
