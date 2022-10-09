package me.kqlqk.behealthy.view.dto.auth;

import lombok.Data;

@Data
public class UserAuthDTO {
    private long id;
    private String name;
    private String email;
    private String password;
}
