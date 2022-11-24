package me.kqlqk.behealthy.view.feign_client;


import feign.Param;
import me.kqlqk.behealthy.view.dto.auth.LoginDTO;
import me.kqlqk.behealthy.view.dto.auth.RegistrationDTO;
import me.kqlqk.behealthy.view.dto.condition.DailyFoodDTO;
import me.kqlqk.behealthy.view.dto.condition.KcalsInfoDTO;
import me.kqlqk.behealthy.view.dto.condition.UserConditionDTO;
import me.kqlqk.behealthy.view.dto.workout.WorkoutInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "gateway")
public interface GatewayClient {

    @PostMapping("/api/v1/login")
    ResponseEntity<?> logIn(@Param LoginDTO loginDTO);

    @PostMapping("/api/v1/registration")
    ResponseEntity<?> registration(@Param RegistrationDTO registrationDTO);

    @GetMapping("/api/v1/users/{id}/condition")
    UserConditionDTO getUserCondition(@PathVariable long id);

    @PostMapping("/api/v1/users/{id}/condition")
    void createUserCondition(@PathVariable long id,
                             @RequestBody UserConditionDTO userConditionDTO);

    @PutMapping("/api/v1/users/{id}/condition")
    void updateUserCondition(@PathVariable long id,
                             @RequestBody UserConditionDTO userConditionDTO);

    @GetMapping("api/v1/users/{id}/food")
    List<DailyFoodDTO> getDailyFood(@PathVariable long id);

    @GetMapping("api/v1/users/{id}/kcals")
    KcalsInfoDTO getDailyKcals(@PathVariable long id);

    @PostMapping("/api/v1/users/{id}/food")
    void addDailyFood(@PathVariable long id,
                      @RequestBody DailyFoodDTO dailyFoodDTO);

    @GetMapping("/api/v1/users/{id}/workout")
    List<WorkoutInfoDTO> getWorkouts(@PathVariable long id);

    @PostMapping("/api/v1/users/{id}/workout")
    void createWorkout(@PathVariable long id,
                       @RequestBody WorkoutInfoDTO workoutInfoDTO);

    @PutMapping("/api/v1/users/{id}/workout")
    void updateWorkout(@PathVariable long id,
                       @RequestBody WorkoutInfoDTO workoutInfoDTO);
}
