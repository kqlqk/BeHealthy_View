package me.kqlqk.behealthy.view.dto.condition;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class UserConditionDTO {
    private long id;

    @Pattern(regexp = "MALE|FEMALE", message = "Please choose gender")
    private String gender;

    @Min(value = 10, message = "you should be over 10 years old")
    @Max(value = 90, message = "you should be under 90 years old")
    private byte age;

    @Min(value = 130, message = "you should be over 130 cm")
    @Max(value = 200, message = "you should be under 200 cm")
    private short height;

    @Min(value = 40, message = "you should weight more than 40 kg")
    @Max(value = 150, message = "you should weight less than 150 kg")
    private short weight;

    @Pattern(regexp = "MIN|AVGG|MAX", message = "Please choose intensity")
    private String intensity;

    @Pattern(regexp = "LOSE|MAINTAIN|GAIN", message = "Please choose goal")
    private String goal;
}
