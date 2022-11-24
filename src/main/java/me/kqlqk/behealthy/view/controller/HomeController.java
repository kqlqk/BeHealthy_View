package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.condition.UserConditionDTO;
import me.kqlqk.behealthy.view.feign_client.GatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class HomeController {
    private final GatewayClient gatewayClient;

    @Autowired
    public HomeController(GatewayClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }

    @GetMapping("/me/{id}")
    public String getCurrentUserPage(@PathVariable long id, Model model) {
        model.addAttribute("userId", id);

        try {
            UserConditionDTO userConditionDTO = gatewayClient.getUserCondition(id);
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
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("setCondition", false);
            model.addAttribute("userId", id);
            return "user/MePage";
        }

        try {
            gatewayClient.createUserCondition(id, userConditionDTO);
        } catch (RuntimeException e) {
            return "redirect:/me/" + id;
        }

        return "redirect:/me/" + id;
    }


    @GetMapping("/me/{id}/edit")
    public String getEditPage(@PathVariable long id, Model model) {
        model.addAttribute("userId", id);

        UserConditionDTO userCondition;
        try {
            userCondition = gatewayClient.getUserCondition(id);
        } catch (RuntimeException e) {
            return "redirect:/me/" + id;
        }

        model.addAttribute("condition", userCondition);

        return "user/EditUserConditionPage";
    }

    @PostMapping("/me/{id}/edit")
    public String editUserCondition(@PathVariable long id,
                                    @ModelAttribute("condition") @Valid UserConditionDTO userCondition,
                                    BindingResult bindingResult,
                                    Model model) {
        model.addAttribute("userId", id);

        if (bindingResult.hasErrors()) {
            return "user/EditUserConditionPage";
        }

        try {
            gatewayClient.updateUserCondition(id, userCondition);
        } catch (RuntimeException e) {
            return "redirect:/me/" + id;
        }


        return "redirect:/me/" + id;
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
