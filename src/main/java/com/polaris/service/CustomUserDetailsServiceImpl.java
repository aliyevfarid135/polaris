package com.polaris.service;

import com.polaris.entity.Learner;
import com.polaris.entity.Mentor;
import com.polaris.repository.LearnerRepository;
import com.polaris.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LearnerRepository learnerRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to find user with email: " + username);

        Learner learner = learnerRepository.findByEmail(username);
        Mentor mentor = mentorRepository.findByEmail(username);

        if (learner != null) {
            System.out.println("Found learner: " + learner.getEmail());
            return buildUserDetails(learner.getEmail(), learner.getPassword(), "ROLE_LEARNER");
        } else if (mentor != null) {
            System.out.println("Found mentor: " + mentor.getEmail());
            return buildUserDetails(mentor.getEmail(), mentor.getPassword(), "ROLE_MENTOR");
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
