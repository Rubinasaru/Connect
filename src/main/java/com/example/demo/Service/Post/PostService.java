package com.example.demo.Service.Post;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.Post.CommentRequestDTO;
import com.example.demo.DTO.request.Post.PostRequestDTO;
import com.example.demo.Models.Post.Comment;
import com.example.demo.Models.Post.Post;

@Service
public interface PostService {
    Post createPost(PostRequestDTO postDTO, Long userId);
    //List<Post> getFeedPosts();
    Long likePost(Long postId, Long userId);
    Comment addComment(Long postId, CommentRequestDTO commentDTO, Long userId);
    void deletePost(Long postId, Long userId);
}

