package com.example.demo.Controller;

import com.example.demo.DTO.request.NotificationDTO;
import com.example.demo.DTO.request.Post.CommentRequestDTO;
import com.example.demo.DTO.request.Post.LikeRequestDTO;
import com.example.demo.DTO.response.PostResponse;
import com.example.demo.Models.Post.Comment;
import com.example.demo.Models.Post.Post;
import com.example.demo.Models.User;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.request.Post.PostRequestDTO;
import com.example.demo.DTO.response.ResponseObject;
import com.example.demo.Service.Post.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name="Post Controller",description="Posts related information")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

//    @PostMapping("/{userId}")
//    @Operation(summary="Post Created by users")
//    public ResponseEntity<ResponseObject> createPost(@RequestBody PostRequestDTO dto,@PathVariable("userId") Long userId) {
//        return ResponseEntity.ok(ResponseObject.success("Post created successfully!", postService.createPost(dto, userId)));
//    }
@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<PostResponse> createPost(
        @RequestPart("content") String content,
        @RequestPart(value = "file", required = false) MultipartFile file,
        @RequestHeader("Authorization") String token) throws IOException {

    PostResponse postDTO = postService.createPost(content, file, token);
    return ResponseEntity.ok(postDTO);
}

    @GetMapping("/feed")
    @Operation(summary="Get all the Feeds")
    public ResponseEntity<List<Post>> getFeedPosts(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(postService.getFeedPosts());
    }

    @PostMapping("/like")
    @Operation(summary="user liked posts")
    public ResponseEntity<String> likePost(@RequestBody LikeRequestDTO likeRequest,Long userId) {

        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found!"));
        postService.likePost(likeRequest.getPostId(), likeRequest.getLikerId());


        Long postOwnerId = postService.getPostOwnerId(likeRequest.getPostId());
        System.out.println(postOwnerId);

        if (!userId.equals(postOwnerId)) {
            NotificationDTO notification = new NotificationDTO();
            notification.setContent(user.getUsername() + " liked your post.");
            notification.setSenderId(likeRequest.getLikerId());
            notification.setRecipientId(postOwnerId);
            notification.setType("LIKE");
            notification.setPostId(likeRequest.getPostId());
            notification.setTimestamp(LocalDateTime.now().toString());

            notificationController.sendNotification(notification);
        }
        return ResponseEntity.ok("Post liked and notification sent.");
    }

    @PostMapping("/comment")
    @Operation(summary = "User commented on post")
    public ResponseEntity<String> commentOnPost(
            @RequestBody CommentRequestDTO commentRequest,
            @RequestParam Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found!"));

        postService.addComment(commentRequest.getPostId(), commentRequest, userId);

        Long postOwnerId = postService.getPostOwnerId(commentRequest.getPostId());

        if (!userId.equals(postOwnerId)) {
            NotificationDTO notification = new NotificationDTO();
            notification.setContent(user.getUsername() + " commented on your post.");
            notification.setSenderId(userId);
            notification.setRecipientId(postOwnerId);
            notification.setType("COMMENT");
            notification.setPostId(commentRequest.getPostId());
            notification.setTimestamp(LocalDateTime.now().toString());

            notificationController.sendNotification(notification);
        }

        return ResponseEntity.ok("Comment added and notification sent.");
    }


    @DeleteMapping("/{postId}")
    @Operation(summary="user deleted post")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.ok("Deleted");
    }
}