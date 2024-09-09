package com.hodoo.hodoomall.auth.controller;

import com.hodoo.hodoomall.auth.service.AuthService;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import com.hodoo.hodoomall.user.service.UserService;
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
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> loginWithEmail(@RequestBody UserDTO userDTO) {

        try {
            UserDTO loginUser = authService.loginWithEmail(userDTO);

            return ResponseEntity.ok().body(Map.of("status", "success", "user", loginUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }


    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> map){


        String token = map.get("token");
        System.out.println(token);
        try {
            UserDTO loginUser = authService.loginWithGoogle(token);

            return ResponseEntity.ok().body(Map.of("status", "success", "user", loginUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }


    }
}
