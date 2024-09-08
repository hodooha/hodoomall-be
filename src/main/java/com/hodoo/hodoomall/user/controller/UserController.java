package com.hodoo.hodoomall.user.controller;

import com.hodoo.hodoomall.auth.service.AuthService;
import com.hodoo.hodoomall.user.model.dto.ResponseUserDTO;
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
    private final AuthService authService;

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

    @GetMapping("/me")
    public ResponseEntity<?> getUser(){

        try{
            UserDTO user = userService.getUser();
            ResponseUserDTO userDTO = new ResponseUserDTO();
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setLevel(user.getLevel());
            return ResponseEntity.ok().body(Map.of("status", "success", "user", userDTO));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

}
