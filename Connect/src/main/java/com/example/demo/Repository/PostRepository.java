package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Models.Post.Post;

@Repository
public interface PostRepository extends JpaRepository <Post,Long> {

	List<Post> findAllByOrderByCreatedAtDesc();

}
