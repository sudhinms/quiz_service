package com.exam.quiz.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String title;

    @ElementCollection
    @Column(name = "questionIds")
    private List<Integer> questionIds;
}
