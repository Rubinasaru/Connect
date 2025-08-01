package com.example.demo.Service.Post;

import com.example.demo.DTO.request.Post.CommentRequestDTO;
import com.example.demo.DTO.response.PostResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.Post.PostRequestDTO;
import com.example.demo.Models.Post.Comment;
import com.example.demo.Models.Post.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PostService {
//    Post createPost(PostRequestDTO postDTO, Long userId);
    PostResponse createPost(String content, MultipartFile file, String token) throws IOException;
    List<Post> getFeedPosts(String token);
    Long likePost(Long postId, Long userId);
    Comment addComment(Long postId, CommentRequestDTO commentDTO, Long userId);
    void deletePost(Long postId, Long userId);

    Long getPostOwnerId(Long postId);

}