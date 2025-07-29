package com.example.demo.DTO.request.Post;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LikeRequestDTO {
    private Long postId;

    private Long likerId; // ID of the user who liked the post

}
