package com.hodoo.hodoomall.user.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
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

}
