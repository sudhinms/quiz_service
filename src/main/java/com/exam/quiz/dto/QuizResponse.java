package com.exam.quiz.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizResponse {

    private Integer id;
    private String title;
    private List<QuestionDto> questions;
}
