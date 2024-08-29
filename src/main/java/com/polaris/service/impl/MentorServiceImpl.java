package com.polaris.service.impl;

import com.polaris.entity.Mentor;
import com.polaris.repository.MentorRepository;
import com.polaris.service.MentorService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@Slf4j
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;
    @Override
    public Page<Mentor> getAllMentors(Pageable pageable) {
        log.info("Fetching all mentors with pagination - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Mentor> mentorPage = mentorRepository.findAll(pageable);
        log.info("Found {} mentors on current page", mentorPage.getNumberOfElements());
        return mentorPage;
    }

    @Override
    public Optional<Mentor> getMentorById(Integer id) {
        log.info("Fetching mentor with ID: {}", id);
        Optional<Mentor> mentor = mentorRepository.findById(id);
        if (mentor.isPresent()) {
            log.info("Mentor found with ID: {}", id);
        } else {
            log.warn("Mentor not found with ID: {}", id);
        }
        return mentor;
    }
}
