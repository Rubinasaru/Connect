package com.example.demo.Service;

import com.example.demo.Enums.UserType;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.utils.TfidfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MentorRecommendationService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired private TfidfUtil tfidfUtil;


//    public List<UserProfile> getAllMentors() {
//        return profileRepository.findAll().stream()
//                .filter(user -> "mentor".equalsIgnoreCase(user.getRole(UserType)))
//                .toList();
//    }
//
//    public List<UserProfile> getAllJuniors() {
//        return profileRepository.findAll().stream()
//                .filter(user -> "junior".equalsIgnoreCase(user.getRole()))
//                .toList();
//    }

    public List<UserProfile> recommendMentors(Long juniorId) {
        UserProfile junior = profileRepository.findById(juniorId)
                .orElseThrow(() -> new RuntimeException("Junior not found"));

        // Normalize junior interests
        String juniorInterests = normalizeInterests(junior.getInterest().toString());

        List<UserProfile> mentors = profileRepository.findByRole(UserType.valueOf("MENTOR"));

        List<MentorScore> scoredMentors = mentors.stream()
                .map(m -> {
                    String mentorInterests = normalizeInterests(m.getInterest().toString());
                    double score = tfidfUtil.computeSimilarity(juniorInterests, mentorInterests);
                    return new MentorScore(m, score);
                })
                .sorted((a, b) -> Double.compare(b.score, a.score))
                .limit(5)
                .collect(Collectors.toList());

        return scoredMentors.stream().map(ms -> ms.user).toList();
    }

    private String normalizeInterests(String interests) {
        return Arrays.stream(interests.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }


    private record MentorScore(UserProfile user, double score) {}
}
