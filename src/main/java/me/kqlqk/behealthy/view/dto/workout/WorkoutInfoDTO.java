package me.kqlqk.behealthy.view.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutInfoDTO {
    private long id;
    private int workoutDay;
    private int numberPerDay;
    private ExerciseDTO exercise;
    private String workoutsPerWeek;
}