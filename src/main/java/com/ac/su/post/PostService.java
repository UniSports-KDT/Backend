package com.ac.su.post;

import com.ac.su.admin.Admin;
import com.ac.su.admin.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AdminRepository adminRepository;

    // 공지사항을 작성하는 메서드
    public Post createPost(Long adminId, String title, String content) {
        // 관리자를 조회.
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 관리자를 찾을 수 없습니다."));

        // 공지사항 생성
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAdmin(admin);

        // 공지사항을 저장하고 반환.
        return postRepository.save(post);
    }

    // 공지사항을 수정하는 메서드
    public Post updatePost(Long announcementId, String title, String content) {
        // 공지사항을 ID로 조회.
        Post post = postRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));

        // 공지사항의 제목과 내용을 업데이트.
        post.setTitle(title);
        post.setContent(content);

        // 수정된 공지사항을 저장하고 반환.
        return postRepository.save(post);
    }

    // 공지사항을 삭제하는 메서드
    public void deletePost(Long announcementId) {
        // 공지사항을 ID로 조회. 없으면 예외를 던진다ㅏㅏㅏ.
        Post post = postRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));

        // 조회된 공지사항을 삭제!.
        postRepository.delete(post);
    }

    // 전체 공지사항을 조회하는 메서드
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
