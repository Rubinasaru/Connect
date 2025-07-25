package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.User;
import com.example.demo.Models.Post.Like;
import com.example.demo.Models.Post.Post;

@Repository
public interface LikeRepository extends JpaRepository <Like,Long> {

	boolean existsByPostAndUser(Post post, User user);

}
