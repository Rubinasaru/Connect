package com.example.demo.DTO.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long postId;
    private String username;
    private String content;
    private String mediaUrl;
    private String mediaType;
    private String profileImgUrl;
    private LocalDateTime createdAt;



//    private int likeCount;
//    private List<String> comments;
}