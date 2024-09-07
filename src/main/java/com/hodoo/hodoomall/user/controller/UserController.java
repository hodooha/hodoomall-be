package com.hodoo.hodoomall.user.controller;

import com.hodoo.hodoomall.user.model.dto.UserDTO;
import com.hodoo.hodoomall.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {

        System.out.println(userDTO);
        try {
            userService.createUser(userDTO);
            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

}
