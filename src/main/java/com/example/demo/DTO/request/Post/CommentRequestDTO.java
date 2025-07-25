package com.example.demo.DTO.request.Post;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private Long postId;
    private String text;
    
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
    
    
}
