package com.example.demo.DTO.request.Post;

import com.example.demo.Enums.PostType;

import lombok.Data;

@Data
public class PostRequestDTO {
    private String content;
    private PostType type;

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public PostType getType() {
        return type;
    }
    public void setType(PostType type) {
        this.type = type;
    }


}