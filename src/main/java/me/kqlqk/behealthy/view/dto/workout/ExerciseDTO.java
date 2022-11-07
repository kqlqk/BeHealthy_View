package me.kqlqk.behealthy.view.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {
    private int id;
    private String name;
    private String description;
    private String muscleGroup;
}