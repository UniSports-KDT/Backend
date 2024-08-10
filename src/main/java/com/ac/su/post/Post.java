package com.ac.su.post;

import com.ac.su.admin.Admin;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    @CreationTimestamp
    private LocalDateTime createdAt; // 생성 날짜
    @UpdateTimestamp
    private LocalDateTime updatedAt; // 수정 날짜

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}