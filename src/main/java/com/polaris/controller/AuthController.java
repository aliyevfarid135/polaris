package com.polaris.controller;

import com.polaris.entity.Category;
import com.polaris.entity.User;
import com.polaris.payload.request.LoginRequest;
import com.polaris.payload.request.SignupRequest;
import com.polaris.payload.response.MessageResponse;
import com.polaris.repository.CategoryRepository;
import com.polaris.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            authentication = authenticationProvider.authenticate(authentication);
            return ResponseEntity.ok(new MessageResponse("Login successful!"));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid email or password."));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Category category = categoryRepository.findById(signUpRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Error: Category not found."));

        User user = User.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .category(category)
                .build();

        user.addRole("ROLE_USER");
        user.addRole("ROLE_LEARNER");

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/login")
    public String getlogin() {
        return "login";
    }

    @GetMapping("/success")
    public String getsuccess() {
        return "success";
    }
}
