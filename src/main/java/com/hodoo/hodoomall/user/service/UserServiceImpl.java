package com.hodoo.hodoomall.user.service;

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

    @Override
    public void createUser(UserDTO userDTO) throws Exception {

        UserDTO existingUser = findByEmail(userDTO.getEmail());
        if (existingUser != null) {
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
        userRepository.save(newUser);

        return new UserDTO(newUser);
    }

    @Override
    public UserDTO findByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);

        if (user == null) throw new Exception("유저가 존재하지 않습니다.");

        UserDTO userDTO = new UserDTO(user);
        userDTO.setPassword(user.getPassword());

        return userDTO;
    }

    @Override
    public UserDTO findById(String id) throws Exception {

        User user = userRepository.findById(id).orElseThrow(() -> new Exception("유저가 존재하지 않습니다."));

        return new UserDTO(user);
    }


}
