package com.example.demo.DTO.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostResponse {
    private Long postId;
    private String username;
    private String content;
    private String imageUrl;
    private int likeCount;
    private List<String> comments;
}