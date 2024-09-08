package com.hodoo.hodoomall.auth.service;

import com.hodoo.hodoomall.user.model.dto.RequestUserDTO;
import com.hodoo.hodoomall.user.model.dto.UserDTO;

public interface AuthService {
    UserDTO loginWithEmail(RequestUserDTO userDTO) throws Exception;
}
