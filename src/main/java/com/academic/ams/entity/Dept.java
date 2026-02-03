package com.academic.ams.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "depts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Dept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;
}

