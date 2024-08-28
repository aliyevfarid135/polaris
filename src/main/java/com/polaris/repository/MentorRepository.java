package com.polaris.repository;


import com.polaris.entity.Mentor;
import com.polaris.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Integer> {
    Boolean existsByEmail(String email);
    Mentor findByEmail(String email);
    Optional<Mentor> findByUser(User user);
}
