package com.polaris.controller;

import com.polaris.entity.Category;
import com.polaris.entity.Mentor;
import com.polaris.entity.Skill;
import com.polaris.payload.request.MentorSignupRequest;
import com.polaris.payload.response.MessageResponse;
import com.polaris.repository.CategoryRepository;
import com.polaris.repository.MentorRepository;
import com.polaris.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mentors")
public class MentorController {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerMentor(@RequestBody MentorSignupRequest signUpRequest) {
        if (mentorRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Category category = categoryRepository.findById(signUpRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Error: Category not found."));

        List<Skill> skills = new ArrayList<>();
        for (String skillName : signUpRequest.getSkills()) {
            Skill skill = new Skill(skillName);
            skill.setMentor(null);
            skillRepository.save(skill);
            skills.add(skill);
        }

        // Şifreyi şifrele
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        Mentor mentor = Mentor.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(encodedPassword) // Şifreyi şifrelenmiş olarak ayarla
                .category(category)
                .skills(skills)
                .build();

        for (Skill skill : skills) {
            skill.setMentor(mentor);
        }
        mentorRepository.save(mentor);
        skillRepository.saveAll(skills);
        return ResponseEntity.ok(new MessageResponse("Mentor registered successfully!"));
    }
}
