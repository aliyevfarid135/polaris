package com.polaris.controller;

import com.polaris.entity.Category;
import com.polaris.entity.Mentor;
import com.polaris.entity.Skill;
import com.polaris.entity.User;
import com.polaris.payload.request.MentorSignupRequest;
import com.polaris.payload.response.MessageResponse;
import com.polaris.repository.CategoryRepository;
import com.polaris.repository.MentorRepository;
import com.polaris.repository.SkillRepository;
import com.polaris.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mentors")
public class MentorController {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @PostMapping("/become-a-mentor")
    public ResponseEntity<?> becomeAMentor(@RequestBody MentorSignupRequest mentorRequest, @AuthenticationPrincipal User user) {
        Optional<Mentor> existingMentor = mentorRepository.findByUser(user);
        if (existingMentor != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is already a mentor!"));
        }
        user.addRole("ROLE_MENTOR");
        userRepository.save(user);
        Mentor mentor = new Mentor();
        mentor.setUser(user);
        mentor.setExperienceYears(mentorRequest.getExperienceYears());
        List<Skill> skills = new ArrayList<>();
        for (String skillName : mentorRequest.getSkills()) {
            Skill skill = skillRepository.findByName(skillName)
                    .orElse(new Skill(skillName));
            skillRepository.save(skill);
            skills.add(skill);
        }
        mentor.setSkills(skills);
        mentor.setAbout(mentorRequest.getAbout());
        mentorRepository.save(mentor);
        return ResponseEntity.ok(new MessageResponse("Mentor profile updated and role assigned successfully!"));
    }
}
