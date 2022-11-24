package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.workout.WorkoutInfoDTO;
import me.kqlqk.behealthy.view.feign_client.GatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkoutController {
    private final GatewayClient gatewayClient;

    @Autowired
    public WorkoutController(GatewayClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }


    @GetMapping("me/{id}/workout")
    public String getWorkout(@PathVariable long id, Model model) {
        model.addAttribute("userId", id);

        List<WorkoutInfoDTO> workoutInfoDTOs;
        try {
            workoutInfoDTOs = gatewayClient.getWorkouts(id);

            List<WorkoutInfoDTO> firstDayWorkouts = new ArrayList<>();
            List<WorkoutInfoDTO> secondDayWorkouts = new ArrayList<>();
            List<WorkoutInfoDTO> thirdDayWorkouts = new ArrayList<>();
            List<WorkoutInfoDTO> fourthDayWorkouts = new ArrayList<>();
            for (WorkoutInfoDTO workoutInfoDTO : workoutInfoDTOs) {
                switch (workoutInfoDTO.getWorkoutDay()) {
                    case 1:
                        firstDayWorkouts.add(workoutInfoDTO);
                        break;
                    case 2:
                        secondDayWorkouts.add(workoutInfoDTO);
                        break;
                    case 3:
                        thirdDayWorkouts.add(workoutInfoDTO);
                        break;
                    case 4:
                        fourthDayWorkouts.add(workoutInfoDTO);
                        break;
                }
            }

            model.addAttribute("setWorkout", true);
            model.addAttribute("day1workouts", firstDayWorkouts);
            model.addAttribute("day2workouts", secondDayWorkouts);
            model.addAttribute("day3workouts", thirdDayWorkouts);
            model.addAttribute("day4workouts", fourthDayWorkouts);
            model.addAttribute("workouts", workoutInfoDTOs);

        } catch (RuntimeException e) {
            model.addAttribute("setWorkout", false);
            model.addAttribute("workout", new WorkoutInfoDTO());
        }

        return "user/WorkoutPage";
    }

    @PostMapping("me/{id}/workout")
    public String createWorkout(@PathVariable long id,
                                @ModelAttribute WorkoutInfoDTO workoutInfoDTO) {
        try {
            gatewayClient.createWorkout(id, workoutInfoDTO);
        } catch (RuntimeException e) {
            return "redirect:/me/" + id + "/workout";
        }

        return "redirect:/me/" + id + "/workout";

    }

    @GetMapping("/me/{id}/workout/edit")
    public String getEditPage(@PathVariable long id, Model model) {
        model.addAttribute("userId", id);

        try {
            gatewayClient.getWorkouts(id);
        } catch (RuntimeException e) {
            return "redirect:/me/" + id + "/workout";
        }

        model.addAttribute("workout", new WorkoutInfoDTO());

        return "user/EditWorkoutPage";
    }

    @PostMapping("/me/{id}/workout/edit")
    public String updateWorkout(@PathVariable long id,
                                @ModelAttribute("workout") WorkoutInfoDTO workoutInfoDTO,
                                Model model) {
        model.addAttribute("userId", id);

        try {
            gatewayClient.updateWorkout(id, workoutInfoDTO);
        } catch (RuntimeException e) {
            return "redirect:/me/" + id + "/workout";
        }

        return "redirect:/me/" + id + "/workout";
    }
}
