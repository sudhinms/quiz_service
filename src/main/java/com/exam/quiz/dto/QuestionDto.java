package com.exam.quiz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionDto {

    private Integer id;
    private String category;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String difficulty;
    private String answer;
}
