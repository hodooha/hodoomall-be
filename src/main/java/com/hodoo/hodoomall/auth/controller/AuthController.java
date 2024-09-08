package com.hodoo.hodoomall.auth.controller;

import com.hodoo.hodoomall.auth.service.AuthService;
import com.hodoo.hodoomall.user.model.dto.RequestUserDTO;
import com.hodoo.hodoomall.user.model.dto.ResponseUserDTO;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> loginWithEmail(@RequestBody RequestUserDTO userDTO) {

        try {
            UserDTO loginUser = authService.loginWithEmail(userDTO);
            ResponseUserDTO responseUser = new ResponseUserDTO();
            responseUser.setEmail(loginUser.getEmail());
            responseUser.setName(loginUser.getName());
            responseUser.setLevel(loginUser.getLevel());
            return ResponseEntity.ok().body(Map.of("status", "success", "token", loginUser.getToken(), "user", responseUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }


    }





//    @PostMapping("/google")

}
