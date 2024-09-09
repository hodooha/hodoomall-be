package com.hodoo.hodoomall.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hodoo.hodoomall.auth.util.JwtTokenProvider;
import com.hodoo.hodoomall.user.model.dao.UserRepository;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import com.hodoo.hodoomall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;

    @Override
    public UserDTO loginWithEmail(UserDTO userDTO) throws Exception {

        UserDTO loginUser = userService.findByEmail(userDTO.getEmail());

        if(loginUser == null){
            throw new Exception("일치하는 이메일 계정이 없습니다.");
        }

        if(!bCryptPasswordEncoder.matches(userDTO.getPassword(), loginUser.getPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(loginUser.getId(), loginUser.getLevel());

        // UserDTO에 토큰 추가
        loginUser.setToken(token);
        loginUser.setPassword(null);

        return loginUser;
    }

    @Override
    public UserDTO loginWithGoogle(String token) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);
        System.out.println(idToken);
        if(idToken == null){
            throw new Exception("Invalid Google token");
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        UserDTO existingUser = userService.findByEmail(email);
        UserDTO loginUser;

        if(existingUser != null){
            loginUser = existingUser;
        } else{
            String randomPassword = "" + Math.floor(Math.random() * 100000000);
            loginUser = userService.createUserWithGoogle(email, name, randomPassword);
        }

        String jwtToken = jwtTokenProvider.createToken(loginUser.getId(), loginUser.getLevel());

        loginUser.setPassword(null);
        loginUser.setToken(jwtToken);

        return loginUser;
    }


}
