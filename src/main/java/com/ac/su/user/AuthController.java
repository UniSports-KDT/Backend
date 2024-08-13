package com.ac.su.user;

import com.ac.su.user.JwtTokenUtil;
import com.ac.su.user.User;
import com.ac.su.user.UserService;
import com.ac.su.user.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    @PostMapping("/api/auth//register")
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
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    @Getter
    public static class JwtResponse {
        private final String token;

        public JwtResponse(String token) {
            this.token = token;
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
