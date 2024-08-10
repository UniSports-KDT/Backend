package com.ac.su.post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    // 공지사항을 작성하는 API 엔드포인트
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
}
