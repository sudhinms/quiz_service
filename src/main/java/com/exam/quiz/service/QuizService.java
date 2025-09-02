package com.exam.quiz.service;


import com.exam.quiz.dto.AnswerResponse;
import com.exam.quiz.dto.QuizResponse;

import java.util.List;

public interface QuizService {

    String createQuiz(String title, String category, Integer size);

    List<QuizResponse> getQuizByTitle(String title);

    Integer calculateScore(List<AnswerResponse> answers);

    List<Integer> getQuestionIds(String category, Integer size);
}
