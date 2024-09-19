package com.hodoo.hodoomall.user.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private String name;
    private String level;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.level = user.getLevel();

    }

}
