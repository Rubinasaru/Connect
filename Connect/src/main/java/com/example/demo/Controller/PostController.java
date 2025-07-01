package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.request.Post.CommentRequestDTO;
import com.example.demo.DTO.request.Post.PostRequestDTO;
import com.example.demo.DTO.response.ResponseObject;
import com.example.demo.Service.UserDetailsImpl;
import com.example.demo.Service.UserDetailsServiceImpl;
import com.example.demo.Service.Post.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name="Post Controller",description="Posts related information")
public class PostController {

	@Autowired
    private PostService postService;

    @PostMapping("/{userId}")
    @Operation(summary="Post Created by users")
    public ResponseEntity<ResponseObject> createPost(@RequestBody PostRequestDTO dto,@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(ResponseObject.success("Post created successfully!", postService.createPost(dto, userId)));
    }

//    @GetMapping("/feed")
//    @Operation(summary="Get all the Feeds")
//    public ResponseEntity<?> getFeed() {
//        return ResponseEntity.ok(postService.getFeedPosts());
//    }

    @PostMapping("/{postId}/like")
    @Operation(summary="user liked posts")
    public ResponseEntity<ResponseObject> likePost(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId) {
 	        
 	    return ResponseEntity.ok(ResponseObject.success("User Liked posts!",postService.likePost(postId, userId)));
 	} 

    @PostMapping("/{postId}/comment")
    @Operation(summary="user Commented on posts")
    public ResponseEntity<ResponseObject> commentPost(@PathVariable("postId") Long postId, @RequestBody CommentRequestDTO dto, @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(ResponseObject.success("User Commented Successfully!",postService.addComment(postId, dto, userId)));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary="user deleted post")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.ok("Deleted");
    }
}

