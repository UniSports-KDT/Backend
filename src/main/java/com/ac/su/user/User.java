package com.ac.su.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //이름
    private String department; //학과 admin은 null값으로 유지
    private String studentId; //학번 admin은 null값으로 유지
    @Column(nullable = false)
    private String password; // 비밀번호
    @Column(nullable = false)
    private String phone; // 휴대폰 번호
    //24.08.12 추가 컬럼
    @Column(nullable = false)
    private String username; //로그인 아이디
    @Enumerated(EnumType.STRING)
    private UserRole userRole; // e.g. GENERAL, ADMIN

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(userRole);
    }
}