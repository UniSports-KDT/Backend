package com.ac.su.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //이름
    private String department; //학과 admin은 null값으로 유지
    private String studentId; //학번 admin은 null값으로 유지
    private String password; // 비밀번호
    private String phone; // 휴대폰 번호
    //24.08.12 추가 컬럼
    private String username; //로그인 아이디
    private UserRole userRole; // GENERAL, ADMIN
}