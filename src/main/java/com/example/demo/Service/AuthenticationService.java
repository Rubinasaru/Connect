package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.ForgotPasswordDTO;
import com.example.demo.DTO.request.LoginRequest;
import com.example.demo.DTO.request.OtpRequest;
import com.example.demo.DTO.request.RegisterRequest;
import com.example.demo.DTO.request.ResetPasswordDTO;
import com.example.demo.DTO.response.RegisterResponseDTO;
import com.example.demo.DTO.response.UserInfoResponse;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private JwtService jwtService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	 @Autowired
	private otpService otpService;

    @Transactional
    public RegisterResponseDTO register(RegisterRequest registerRequest) {
        validateSignupRequest(registerRequest);
        User user = createUserFromRequest(registerRequest);
        User savedUser = userRepository.save(user);
        return new RegisterResponseDTO(savedUser);
    }
    
    private void validateSignupRequest(RegisterRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Signup request cannot be null or missing required fields!");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username (" + request.getUsername() +") " + " is already taken!");
        }
    }
    
    private User createUserFromRequest(RegisterRequest request) {
        return new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
//                AuthProvider.LOCAL
        );
    }

    @Transactional
    public UserInfoResponse login(LoginRequest loginRequest){
        // Check if user exists and email is verified
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password!"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwtToken = jwtService.generateTokenFromUsername(userDetails);
            return new UserInfoResponse(jwtToken, userDetails.getUsername());
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid username or password!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Authentication failed: " + e.getMessage() + "!");
        }
    }
    
//    @Transactional
//    public void resetPassword(ResetPasswordDTO dto) {
//        if (dto == null || dto.getEmail() == null || dto.getNewPassword() == null || dto.getConfirmNewPassword() == null) {
//            throw new IllegalArgumentException("All fields are required!");
//        }
//
//        if (!dto.isPasswordMatch()) {
//            throw new IllegalArgumentException("Passwords do not match!");
//        }
//
//        UserProfile profile = profileRepository.findByEmail(dto.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("Email not registered!"));
//
//        User user = profile.getUser();
//        if (user == null) {
//            throw new IllegalStateException("User not found!");
//        }
//
//        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
//        userRepository.save(user);
//    }
    
    @Transactional
    public void generateOtpForRegistration(OtpRequest otpRequest) throws Exception {
        if (otpRequest == null || otpRequest.getEmail() == null || otpRequest.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required!");
        }
        UserProfile user = profileRepository.findByEmail(otpRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + otpRequest.getEmail() + "!"));
        otpService.sendOtpEmail(otpRequest.getEmail());
    }
}

