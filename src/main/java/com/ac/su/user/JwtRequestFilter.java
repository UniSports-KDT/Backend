package com.ac.su.user;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Lazy
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // JWT 토큰을 검증하고 사용자 인증 정보를 SecurityContext에 설정
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            // JWT 페이로드 디코딩 및 출력
            try {
                String[] parts = jwt.split("\\.");
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
                System.out.println("Decoded JWT Payload: " + payload);

                try {
                    username = jwtTokenUtil.extractUsername(jwt);
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT 토큰이 만료되었습니다: " + e.getMessage());
                } catch (MalformedJwtException e) {
                    System.out.println("JWT 토큰이 올바른 형식이 아닙니다: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("JWT 토큰이 잘못되었습니다: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("JWT 토큰에서 사용자 이름을 추출할 수 없습니다: " + e.getMessage());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // JWT 토큰 유효성 검사
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            chain.doFilter(request, response);
        }
    }
}
