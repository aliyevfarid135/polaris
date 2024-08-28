package com.polaris.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "category_id")
    public Category category;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;

    public Mentor(String firstName, String lastName, String email, String encode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = encode;
    }
}
