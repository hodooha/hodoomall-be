package com.hodoo.hodoomall.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hodoo.hodoomall.auth.model.dao.RefreshTokenRepository;
import com.hodoo.hodoomall.auth.model.dto.RefreshToken;
import com.hodoo.hodoomall.auth.model.dto.TokenDTO;
import com.hodoo.hodoomall.auth.util.JwtTokenProvider;
import com.hodoo.hodoomall.user.model.dto.UserDTO;
import com.hodoo.hodoomall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;

    @Override
    public UserDTO loginWithEmail(UserDTO userDTO) throws Exception {

        UserDTO loginUser = userService.getUserByEmail(userDTO.getEmail());
        if (loginUser == null) throw new Exception("계정이 존재하지 않습니다.");
        if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), loginUser.getPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createAccessToken(loginUser.getId(), loginUser.getLevel());
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUser.getId());

        // UserDTO에 토큰 추가
        loginUser.setToken(token);
        loginUser.setPassword(null);
        loginUser.setRefreshToken(refreshToken);

        return loginUser;
    }

    @Override
    public UserDTO loginWithGoogle(String token) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();
        GoogleIdToken idToken = verifier.verify(token);

        if (idToken == null) {
            throw new Exception("Invalid Google token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        UserDTO loginUser = userService.getUserByEmail(email);

        if (loginUser == null) {
            String randomPassword = "" + Math.floor(Math.random() * 100000000);
            loginUser = userService.createUserWithGoogle(email, name, randomPassword);
        }

        String jwtToken = jwtTokenProvider.createAccessToken(loginUser.getId(), loginUser.getLevel());
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUser.getId());
        loginUser.setPassword(null);
        loginUser.setToken(jwtToken);
        loginUser.setRefreshToken(refreshToken);

        return loginUser;
    }

    @Override
    public TokenDTO refresh(TokenDTO tokens) throws Exception {

        if (tokens.getAccessToken().isEmpty() || tokens.getRefreshToken().isEmpty()) {
            throw new Exception("Tokens not found");
        }

        String userId = jwtTokenProvider.getUserId(tokens.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId).orElseThrow(() -> new Exception("Refresh Token expired or not found"));

        if (!refreshToken.getRefreshToken().equals(tokens.getRefreshToken()) || !jwtTokenProvider.validateToken(refreshToken.getRefreshToken())) {
            throw new Exception("InValid Refresh Token");
        }

        String userLevel = jwtTokenProvider.getRole(tokens.getAccessToken()).equals("ROLE_ADMIN") ? "admin" : "customer";
        String newAccessToken = jwtTokenProvider.createAccessToken(userId, userLevel);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

        return new TokenDTO(newAccessToken, newRefreshToken);
    }


}
