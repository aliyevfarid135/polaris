package com.polaris.controller;

import com.polaris.entity.Category;
import com.polaris.entity.Learner;
import com.polaris.payload.request.LearnerSignupRequest;
import com.polaris.payload.request.LoginRequest;
import com.polaris.payload.response.MessageResponse;
import com.polaris.repository.CategoryRepository;
import com.polaris.repository.LearnerRepository;
import com.polaris.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learners")
public class LearnerController {

    @Autowired
    private LearnerRepository learnerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerLearner(@RequestBody LearnerSignupRequest signUpRequest) {
        if (learnerRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Category category = categoryRepository.findById(signUpRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Error: Category not found."));

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        Learner learner = Learner.builder()
                .first_name(signUpRequest.getFirstName())
                .last_name(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(encodedPassword)
                .category(category)
                .build();

        learnerRepository.save(learner);

        return ResponseEntity.ok(new MessageResponse("Learner registered successfully!"));
    }
}
