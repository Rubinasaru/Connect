package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.Models.Post.Post;

@Repository
public interface PostRepository extends JpaRepository <Post,Long> {

	List<Post> findAllByOrderByCreatedAtDesc();

	@Query("SELECT p.user.id FROM Post p WHERE p.id = :postId")
	Long findUserIdById(Long postId);
}
