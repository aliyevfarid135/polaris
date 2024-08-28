package com.polaris.repository;


import com.polaris.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearnerRepository extends JpaRepository<Learner, Integer> {

    Boolean existsByEmail(String email);

    Learner findByEmail(String username);
}
