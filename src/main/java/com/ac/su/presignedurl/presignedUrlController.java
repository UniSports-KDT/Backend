package com.ac.su.presignedurl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class presignedUrlController {

    private final S3Service s3Service;
    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam("filename") String filename){
        var result = s3Service.createPresignedUrl("test/" + filename);
        return result;
    }
}
