package com.ac.su.admin;

import com.ac.su.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; //이름
    private String username; //아이디
    private String password; //비밀번호
    private String phone; //폰 번호

//    //24.08.12 admin 엔티티 삭제하기로함
//    @JsonIgnore
//    @OneToMany(mappedBy = "admin")
//    private List<Post> post; //작성한 공지사항 모음
}