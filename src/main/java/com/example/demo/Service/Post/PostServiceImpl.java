package com.example.demo.Service.Post;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.DTO.request.Post.CommentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Post createPost(PostRequestDTO dto, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found!"));
        Post post = new Post();
        post.setContent(dto.getContent());
        post.setType(dto.getType());
        post.setUser(user);
        return postRepo.save(post);
    }

    @Override
    public List<Post> getFeedPosts() {
        return postRepo.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Long likePost(Long postId, Long userId) {
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
        comment.setText(dto.getText());
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
}