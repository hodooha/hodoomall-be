package com.hodoo.hodoomall.auth.controller;

import com.hodoo.hodoomall.auth.service.AuthService;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok().body(Map.of("status", "success", "user", loginUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> map) {
        String token = map.get("token");
        try {
            UserDTO loginUser = authService.loginWithGoogle(token);
            return ResponseEntity.ok().body(Map.of("status", "success", "user", loginUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {

        try {
            String tokenString = request.getHeader("Authorization");
            String expiredToken = tokenString != null && tokenString.startsWith("Bearer ") ? tokenString.replace("Bearer ", "") : null;
            String newAccessToken = authService.refresh(expiredToken);
            return ResponseEntity.ok().body(Map.of("status", "success", "token", newAccessToken));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }
}
