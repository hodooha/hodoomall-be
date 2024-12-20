package com.hodoo.hodoomall.auth.service;

import com.hodoo.hodoomall.auth.model.dto.TokenDTO;
import com.hodoo.hodoomall.user.model.dto.UserDTO;

public interface AuthService {
    UserDTO loginWithEmail(UserDTO userDTO) throws Exception;

    UserDTO loginWithGoogle(String token) throws Exception;

    TokenDTO refresh(TokenDTO tokens) throws Exception;
}
