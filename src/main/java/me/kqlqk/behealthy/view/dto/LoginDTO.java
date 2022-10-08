package me.kqlqk.behealthy.view.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank(message = "Please enter your email")
    private String email;

    @NotBlank(message = "Please enter your password")
    private String password;
}
