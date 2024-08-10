package com.ac.su.post;

import lombok.Data;

@Data
public class PostDTO {
    private Long adminId; // 관리자 ID
    private String title; // 공지사항 제목
    private String content; // 공지사항 내용
}