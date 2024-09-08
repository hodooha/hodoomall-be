package com.hodoo.hodoomall.user.service;

import com.hodoo.hodoomall.user.model.dao.UserRepository;
import com.hodoo.hodoomall.user.model.dto.CustomUserDetails;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void createUser(UserDTO userDTO) throws Exception{

        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if(existingUser != null){
            throw new Exception("계정이 이미 존재합니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());

        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setName(userDTO.getName());
        newUser.setLevel(userDTO.getLevel() != null ? userDTO.getLevel() : "customer");

        userRepository.save(newUser);
    }

    @Override
    public UserDTO getUser() throws Exception {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userDetails.getUser().getEmail());
        userDTO.setName(userDetails.getUser().getName());
        userDTO.setLevel(userDetails.getUser().getLevel());

        return userDTO;
    }


}
