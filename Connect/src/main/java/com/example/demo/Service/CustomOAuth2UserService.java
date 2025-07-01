package com.example.demo.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.Enums.AuthProvider;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.Repository.UserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository userProfileRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String Username = oAuth2User.getAttribute("name"); // or use "given_name", "family_name"
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        //if profile exists by email
        Optional<UserProfile> profileOpt = userProfileRepository.findByEmail(email);
        if (profileOpt.isPresent()) {
            return oAuth2User; // user already exists
        }

        // Create new User
        User user = new User();
        user.setUsername(Username); 
        user.setPassword(null);  
//        user.setProvider(AuthProvider.GOOGLE);

        // Save User first
        User savedUser = userRepository.save(user);

        // Create UserProfile
        UserProfile profile = new UserProfile();
        profile.setEmail(email);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setMiddleName(null);
        profile.setUser(savedUser);

        userProfileRepository.save(profile);

        return oAuth2User;
    }
}

