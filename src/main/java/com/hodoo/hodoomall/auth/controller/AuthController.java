package com.hodoo.hodoomall.auth.controller;

import com.hodoo.hodoomall.auth.service.AuthService;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> loginWithEmail(@RequestBody UserDTO userDTO) {

        try {
            UserDTO loginUser = authService.loginWithEmail(userDTO);

            if (loginUser != null) {
                return ResponseEntity.ok().body(Map.of("status", "success", "token", loginUser.getToken(), "user", loginUser));
            } else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "fail", "error","일치하는 이메일 계정이 없습니다."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }


    }


//    @PostMapping("/google")

}
