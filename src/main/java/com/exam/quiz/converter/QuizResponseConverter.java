package com.exam.quiz.converter;


import com.exam.quiz.dto.QuizResponse;
import com.exam.quiz.entity.Quiz;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizResponseConverter implements Converter<Quiz, QuizResponse> {


    @Override
    public QuizResponse convert(Quiz source) {
        return QuizResponse.builder()
                .id(source.getId())
                .title(source.getTitle())
//                .questions(source.getQuestions())
                .build();
    }

    public List<QuizResponse> convert(List<Quiz> quizzes){
        return quizzes.stream().map(this::convert).toList();
    }
}
