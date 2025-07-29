package com.example.demo.Controller;

import com.example.demo.DTO.request.UserDTO;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Service.MentorRecommendationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@Tag(name = "Mentor Recommendation Controller", description = "Recommendation related information")
@RequiredArgsConstructor
public class MentorRecommendationController {

    @Autowired
    private MentorRecommendationService recommender;

    @GetMapping("/mentors/{juniorId}")
    public ResponseEntity<List<UserProfile>> recommendMentors(@PathVariable("juniorId") Long juniorId) {
        List<UserProfile> mentors = recommender.recommendMentors(juniorId);
        return ResponseEntity.ok(mentors);
    }
}


