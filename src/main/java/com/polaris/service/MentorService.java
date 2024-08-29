package com.polaris.service;


import com.polaris.entity.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MentorService {
Page<Mentor> getAllMentors(Pageable pageable);
    Optional<Mentor> getMentorById(Integer id);
}
