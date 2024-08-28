package com.polaris.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    public Skill(String name, Mentor mentor) {
        this.name = name;
        this.mentor = mentor;
    }

    public Skill(String skillName) {
        this.name = skillName;
    }
}
