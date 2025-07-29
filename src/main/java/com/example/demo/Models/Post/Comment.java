package com.example.demo.Models.Post;

import java.time.LocalDateTime;

import com.example.demo.Models.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name="comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

	@ManyToOne
	@JoinColumn(name = "user_id")
    private User user;

    private String content;

    private LocalDateTime commentedAt = LocalDateTime.now();
    
    public Long getId() {
  		return id;
  	}

  	public void setId(Long id) {
  		this.id = id;
  	}

  	public Post getPost() {
  		return post;
  	}

  	public void setPost(Post post) {
  		this.post = post;
  	}

  	public User getUser() {
  		return user;
  	}

  	public void setUser(User user) {
  		this.user = user;
  	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCommentedAt() {
  		return commentedAt;
  	}

  	public void setCommentedAt(LocalDateTime commentedAt) {
  		this.commentedAt = commentedAt;
  	}


}

