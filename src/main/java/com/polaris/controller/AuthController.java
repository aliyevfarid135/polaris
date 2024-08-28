package com.polaris.controller;

import com.polaris.payload.request.LoginRequest;
import com.polaris.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationProvider authenticationProvider;

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

    @GetMapping("/login")
    public String getlogin() {
        return "login";
    }

    @GetMapping("/success")
    public String getsuccess() {
        return "success";
    }
}
