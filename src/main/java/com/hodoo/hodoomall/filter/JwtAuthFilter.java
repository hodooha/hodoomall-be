package com.hodoo.hodoomall.filter;

import com.hodoo.hodoomall.auth.util.JwtTokenProvider;
import com.hodoo.hodoomall.user.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/api/auth/refresh")) {
            filterChain.doFilter(request, response); // 다음 필터로 넘기기
            return;
        }

        // Authorization 헤더에서 토큰 추출
        String tokenString = request.getHeader("Authorization");

        if (tokenString != null && tokenString.startsWith("Bearer ")) {
            String token = tokenString.replace("Bearer ", "");
            System.out.println("token" +token);

            if (token.equals("null") || token.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
            // 토큰 유효성 검사
            if (!jwtTokenProvider.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access Token is expired or invalid");
                return;
            }

            try {
                // 토큰에서 사용자 ID 추출
                String userId = jwtTokenProvider.getUserId(token);
                // 사용자 정보 로드
                UserDetails userDetails = customUserDetailService.loadUserByUsername(userId);

                // Spring Security의 인증 객체 설정
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                //현재 Request의 Security Context에 접근권한 설정
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } catch (Exception e) {
                System.out.println("인증 오류 : " + e.getMessage());
            }

        }
        filterChain.doFilter(request, response); // 다음 필터로 넘기기
    }
}
