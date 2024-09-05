package com.hodoo.hodoomall.auth.service;

import com.hodoo.hodoomall.auth.util.JwtTokenProvider;
import com.hodoo.hodoomall.user.model.dao.UserRepository;
import com.hodoo.hodoomall.user.model.dto.User;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDTO loginWithEmail(UserDTO userDTO) throws Exception {

        User user = userRepository.findByEmail(userDTO.getEmail());

        if(user == null){
            throw new Exception("일치하는 이메일 계정이 없습니다.");
        }

        if(!bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(user.getId());

        // UserDTO에 토큰 추가
        UserDTO loginUser = new UserDTO();
        loginUser.setEmail(user.getEmail());
        loginUser.setName(user.getName());
        loginUser.setLevel(user.getLevel());
        loginUser.setToken(token);

        return loginUser;
    }
}
