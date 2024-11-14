package com.hodoo.hodoomall.auth.controller;

import com.hodoo.hodoomall.auth.model.dto.TokenDTO;
import com.hodoo.hodoomall.auth.service.AuthService;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<?> loginWithEmail(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        try {
            UserDTO loginUser = authService.loginWithEmail(userDTO);
            Cookie cookie = new Cookie("refreshToken", userDTO.getRefreshToken());
            cookie.setMaxAge(14*24*60*60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "None");
            response.addCookie(cookie);
            userDTO.setRefreshToken(null);

            return ResponseEntity.ok().body(Map.of("status", "success", "user", loginUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> map, HttpServletResponse response) {
        String token = map.get("token");
        try {
            UserDTO loginUser = authService.loginWithGoogle(token);
            Cookie cookie = new Cookie("refreshToken", loginUser.getRefreshToken());
            cookie.setMaxAge(14*24*60*60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "None");
            response.addCookie(cookie);
            loginUser.setRefreshToken(null);

            return ResponseEntity.ok().body(Map.of("status", "success", "user", loginUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request, HttpServletResponse response) {

        try {
            String tokenString = request.getHeader("Authorization");
            String expiredToken = tokenString != null && tokenString.startsWith("Bearer ") ? tokenString.replace("Bearer ", "") : null;
            TokenDTO tokens = authService.refresh(new TokenDTO(expiredToken, refreshToken));
            Cookie cookie = new Cookie("refreshToken", tokens.getRefreshToken());
            cookie.setMaxAge(14*24*60*60);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "None");
            response.addCookie(cookie);

            return ResponseEntity.ok().body(Map.of("status", "success", "token", tokens.getAccessToken()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }

    }
}
