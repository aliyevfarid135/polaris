package com.polaris.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "mentor_skills",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    private Integer experienceYears;

    private String about;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
