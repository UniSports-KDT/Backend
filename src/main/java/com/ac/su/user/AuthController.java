package com.ac.su.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    //회원가입 api
    @PostMapping("/api/auth/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user.getName(), user.getUsername(), user.getPassword(), user.getPhone(), user.getUserRole());
    }
    //로그인 api
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록되지 않은 유저입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 오류입니다.");
        }
        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        final User user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다: " + loginRequest.getUsername()));

        // Generate JWT token and retrieve user role
        final String jwt = jwtTokenUtil.generateToken(userDetails, user.getId());
        final UserRole userRole = user.getUserRole();


        return ResponseEntity.ok(new JwtResponse(jwt, userRole));
    }
    @Getter
    public static class JwtResponse {
        private final String token;
        private final UserRole userRole;

        public JwtResponse(String token, UserRole userRole) {
            this.token = token;
            this.userRole = userRole;
        }
    }
}

@Getter
@Setter
class LoginRequest {
    private String username;
    private String password;
    private String name;
    private UserRole userRole;
}
