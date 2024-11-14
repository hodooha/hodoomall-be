package com.hodoo.hodoomall.user.service;

import com.hodoo.hodoomall.auth.model.dao.RefreshTokenRepository;
import com.hodoo.hodoomall.user.model.dao.UserRepository;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void createUser(UserDTO userDTO) throws Exception {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
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
    public UserDTO getUser(User user) throws Exception {
        return new UserDTO(user);
    }

    @Override
    public UserDTO createUserWithGoogle(String email, String name, String randomPassword) throws Exception {

        String encodedPassword = bCryptPasswordEncoder.encode(randomPassword);
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(encodedPassword);

        return new UserDTO(userRepository.save(newUser));
    }

    @Override
    public UserDTO getUserByEmail(String email) throws Exception {
        return new UserDTO(userRepository.findByEmail(email));
    }

    @Override
    public UserDTO findById(String id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("유저가 존재하지 않습니다."));
        return new UserDTO(user);
    }

    @Override
    public void logout(User user) throws Exception {
        refreshTokenRepository.deleteById(user.getId());
    }


}
