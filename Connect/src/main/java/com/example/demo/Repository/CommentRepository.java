package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Post.Comment;

@Repository
public interface CommentRepository extends JpaRepository <Comment,Long> {

}
