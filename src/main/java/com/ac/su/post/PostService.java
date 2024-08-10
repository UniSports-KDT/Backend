package com.ac.su.post;

import com.ac.su.admin.Admin;
import com.ac.su.admin.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AdminRepository adminRepository;

    // 공지사항을 작성하는 메서드
    public Post createPost(Long adminId, String title, String content) {
        // 관리자를 조회합니다.
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 관리자를 찾을 수 없습니다."));

        // 공지사항 생성
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAdmin(admin);

        // 공지사항을 저장하고 반환합니다.
        return postRepository.save(post);
    }
}
