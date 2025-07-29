package com.example.demo.Service.Post;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.demo.DTO.request.Post.CommentRequestDTO;
import com.example.demo.DTO.response.PostResponse;
import com.example.demo.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.Post.PostRequestDTO;
import com.example.demo.Models.User;
import com.example.demo.Models.Post.Comment;
import com.example.demo.Models.Post.Like;
import com.example.demo.Models.Post.Post;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.LikeRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LikeRepository likeRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private JwtService jwtService;

//    @Override
//    public Post createPost(PostRequestDTO dto, Long userId) {
//        User user = userRepo.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found!"));
//        Post post = new Post();
//        post.setContent(dto.getContent());
//        post.setType(dto.getType());
//        post.setUser(user);
//        return postRepo.save(post);
//    }

    private final String uploadDir = "uploads/";

    @Override
    public PostResponse createPost(String content, MultipartFile file, String token) throws IOException {
        Post post = new Post();
        post.setContent(content);

        if (file != null && !file.isEmpty()) {
            String savedFile=saveFile(file);
            post.setMediaUrl(savedFile);

            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image"))
                post.setMediaType("image");
            else if (contentType != null && contentType.startsWith("video"))
                post.setMediaType("video");
            else if(contentType == null && contentType.isEmpty())
                post.setMediaType("text");
        }

        // set user from JWT
        User user = getUserFromToken(token);
        post.setUser(user);

        Post saved = postRepo.save(post);

        return new PostResponse(saved.getId(),  user.getUsername(),saved.getContent(), saved.getMediaUrl(),
                saved.getMediaType(),saved.getCreatedAt());
    }

    public String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);

        Files.copy(file.getInputStream(), filePath);

        return filename;  // store this filename in DB or response
    }

    private User getUserFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userEmail;
        try {
            userEmail = jwtService.getEmailFromJwtToken(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token: " + e.getMessage(), e);
        }

        return userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
    }


    @Override
    public List<Post> getFeedPosts() {
        return postRepo.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public  Long likePost(Long postId, Long userId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new RuntimeException("Post not found!"));
        User user = userRepo.findById(userId).orElseThrow(()-> new RuntimeException("User NOT Found!"));
        if (likeRepo.existsByPostAndUser(post, user))
            return -1L;

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        like.setLikedAt(LocalDateTime.now());

        Like saved = likeRepo.save(like);
        return saved.getId();
    }

    @Override
    public Comment addComment(Long postId, CommentRequestDTO dto, Long userId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new RuntimeException("Post Not Found!"));
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User Not Found!"));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(dto.getContent());
        comment.setCommentedAt(LocalDateTime.now());
        return commentRepo.save(comment);
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new RuntimeException("Post Not Found!"));
//        User user = post.getUser();
//        if (user == null || user.getId() == null || !user.getId().equals(userId)) {
//            throw new AccessDeniedException("Not authorized");
//        }
        postRepo.delete(post);
    }

    @Override
    public Long getPostOwnerId(Long postId) {
        return postRepo.findUserIdById(postId);
    }
}