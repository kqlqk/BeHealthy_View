package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.condition.DailyFoodDTO;
import me.kqlqk.behealthy.view.dto.condition.KcalsInfoDTO;
import me.kqlqk.behealthy.view.feign_client.GatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class KcalsCounterController {
    private final GatewayClient gatewayClient;

    @Autowired
    public KcalsCounterController(GatewayClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }

    @GetMapping("/me/{id}/food")
    public String getDailyFood(@PathVariable long id, Model model) {
        model.addAttribute("userId", id);

        try {
            gatewayClient.getUserCondition(id);
        } catch (RuntimeException e) {
            return "redirect:/me/" + id;
        }

        List<DailyFoodDTO> dailyFood = gatewayClient.getDailyFood(id);
        KcalsInfoDTO kcalsInfo = gatewayClient.getDailyKcals(id);

        short dailyKcals = (short) (kcalsInfo.getProtein() * 4 + kcalsInfo.getFat() * 9 + kcalsInfo.getCarb() * 4);
        int ateKcals = 0;

        for (DailyFoodDTO dailyFoodDTO : dailyFood) {
            ateKcals += dailyFoodDTO.getWeight() * dailyFoodDTO.getKcals() / 100;
        }

        model.addAttribute("dailyKcals", dailyKcals);
        model.addAttribute("dailyFood", dailyFood);
        model.addAttribute("newFood", new DailyFoodDTO());
        model.addAttribute("ateKcals", ateKcals);

        return "user/FoodPage";
    }

    @PutMapping("me/{id}/food")
    public String addFood(@PathVariable long id,
                          @ModelAttribute("newFood") @Valid DailyFoodDTO dailyFoodDTO,
                          BindingResult bindingResult,
                          Model model) {
        try {
            gatewayClient.getUserCondition(id);
        } catch (RuntimeException e) {
            return "redirect:/me/" + id;
        }

        if (bindingResult.hasErrors()) {
            List<DailyFoodDTO> dailyFood = gatewayClient.getDailyFood(id);
            KcalsInfoDTO kcalsInfo = gatewayClient.getDailyKcals(id);

            short dailyKcals = (short) (kcalsInfo.getProtein() * 4 + kcalsInfo.getFat() * 9 + kcalsInfo.getCarb() * 4);
            int ateKcals = 0;

            for (DailyFoodDTO element : dailyFood) {
                ateKcals += element.getWeight() * element.getKcals() / 100;
            }
            model.addAttribute("dailyFood", dailyFood);
            model.addAttribute("dailyKcals", dailyKcals);
            model.addAttribute("userId", id);
            model.addAttribute("ateKcals", ateKcals);
            return "user/FoodPage";
        }

        dailyFoodDTO.setUserId(id);
        gatewayClient.addDailyFood(id, dailyFoodDTO);

        return "redirect:/me/" + id + "/food";
    }
}
