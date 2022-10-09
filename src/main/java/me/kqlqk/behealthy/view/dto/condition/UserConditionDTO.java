package me.kqlqk.behealthy.view.dto.condition;

import lombok.Data;

@Data
public class UserConditionDTO {
    private long id;
    private String gender;
    private byte age;
    private short height;
    private short weight;
    private String intensity;
    private String goal;
}
