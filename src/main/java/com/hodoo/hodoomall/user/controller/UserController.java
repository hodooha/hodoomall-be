package com.hodoo.hodoomall.user.controller;

import com.hodoo.hodoomall.user.model.dto.CustomUserDetails;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import com.hodoo.hodoomall.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {

        try {
            userService.createUser(userDTO);
            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        try{
            UserDTO user = userService.getUser(customUserDetails.getUser());

            return ResponseEntity.ok().body(Map.of("status", "success", "user", user));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "error", e.getMessage()));
        }
    }

}
