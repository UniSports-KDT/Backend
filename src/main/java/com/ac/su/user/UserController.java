package com.ac.su.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // 유저 조회 JWT 토큰에서 userId 추출)
    @GetMapping("/api/users")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        // 토큰의 Bearer 부분 제거
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                // JWT 토큰에서 userId 추출
                Long userId = jwtTokenUtil.extractUserId(jwtToken);

                // userId로 사용자 정보 조회
                User user = userService.getUserById(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("UserId를 찾을 수 없습니다 : " + userId));

                return ResponseEntity.ok(user);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT 토큰이 유효하지 않습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT 토큰이 필요합니다.");
        }
    }
}

