package com.hodoo.hodoomall.user.service;

import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.user.model.dto.UserDTO;

public interface UserService {
    void createUser(UserDTO userDTO) throws Exception;

    UserDTO getUser(User user) throws Exception;

    UserDTO createUserWithGoogle(String email, String name, String randomPassword) throws Exception;

    UserDTO getUserByEmail(String email) throws Exception;

    UserDTO findById(String id) throws Exception;

    void logout(User user) throws Exception;
}
