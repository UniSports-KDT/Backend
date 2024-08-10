package com.ac.su.post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    // 공지사항을 작성
    @PostMapping("/api/announcements")
    public ResponseEntity<Post> createAnnouncement(@RequestBody PostDTO postDTO) {
        // 공지사항을 작성합니다.
        Post post = postService.createPost(postDTO.getAdminId(), postDTO.getTitle(), postDTO.getContent());

//        // JSON 형식으로 메시지 반환
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "공지사항 작성 완료!");
//        return ResponseEntity.ok(response);

        // 작성된 공지사항을 반환합니다.
        return ResponseEntity.ok(post);
    }

    // 공지사항을 수정
    @PutMapping("/api/announcements/{announcementId}")
    public ResponseEntity<Post> updateAnnouncement(
            @PathVariable Long announcementId,
            @RequestBody PostDTO postDTO) {

        // 공지사항을 수정.
        Post updatedPost = postService.updatePost(announcementId, postDTO.getTitle(), postDTO.getContent());

        // 수정된 공지사항을 반환.
        return ResponseEntity.ok(updatedPost);
    }

    // 공지사항 삭제
    @DeleteMapping("/api/announcements/{announcementId}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long announcementId) {

        // 공지사항을 삭제!!!
        postService.deletePost(announcementId);

        // JSON 형식으로 메시지 반환
        Map<String, String> response = new HashMap<>();
        response.put("message", "공지사항 삭제 완료!");
        return ResponseEntity.ok(response);
    }

    // 전체 공지사항을 조회
    @GetMapping
    public ResponseEntity<List<Post>> getAllAnnouncements() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
}
