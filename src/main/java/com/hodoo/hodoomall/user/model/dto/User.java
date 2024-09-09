package com.hodoo.hodoomall.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String level = "customer";

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
