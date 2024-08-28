package com.polaris.service;

import com.polaris.entity.Mentor;
import com.polaris.entity.User;
import com.polaris.repository.UserRepository;
import com.polaris.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to find user with email: " + username);

        // Find the user by email
        User user = userRepository.findByEmail(username);

        // Check if the user is a mentor
        Optional<Mentor> mentor = mentorRepository.findByUser(user);

        if (user != null) {
            // Determine the role based on whether the user is a mentor
            String role = (mentor != null) ? "ROLE_MENTOR" : "ROLE_LEARNER";

            System.out.println("Found user: " + user.getEmail() + " with role: " + role);
            return buildUserDetails(user.getEmail(), user.getPassword(), role);
        } else {
            System.out.println("User not found: " + username);
            throw new UsernameNotFoundException("User not found.");
        }
    }

    private UserDetails buildUserDetails(String username, String password, String... authorities) {
        org.springframework.security.core.userdetails.User.UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(username);

        builder.disabled(false);
        builder.password(password);
        builder.authorities(authorities);

        return builder.build();
    }
}
