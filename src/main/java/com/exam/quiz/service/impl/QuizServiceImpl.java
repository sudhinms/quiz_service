package com.exam.quiz.service.impl;

import com.exam.quiz.converter.QuizResponseConverter;
import com.exam.quiz.dto.AnswerResponse;
import com.exam.quiz.dto.QuestionDto;
import com.exam.quiz.dto.QuizResponse;
import com.exam.quiz.entity.Quiz;
import com.exam.quiz.feign.QuestionFeignController;
import com.exam.quiz.repositories.QuizRepository;
import com.exam.quiz.service.QuizService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class QuizServiceImpl implements QuizService {


    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizResponseConverter responseConverter;

    @Autowired
    QuestionFeignController questionFeignController;

    @Override
    @Transactional
    public String createQuiz(String title, String category, Integer size) {
        try {
            List<Integer> questionIds = questionFeignController.getQuestionIds(category, size);

            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestionIds(questionIds);
            quizRepository.save(quiz);

            return "success";
        } catch (Exception e){
            log.error(e.getMessage());
            return "Failed";
        }

    }

    @Override
    public List<QuizResponse> getQuizByTitle(String title) {
        List<Quiz> quizzes = quizRepository.findByTitle(title);
        if (CollectionUtils.isEmpty(quizzes)){
            return Collections.EMPTY_LIST;
        }
        List<QuizResponse> quizResponses = new ArrayList<>(quizzes.size());
        for (Quiz quiz : quizzes) {
            List<Integer> questionIds = quiz.getQuestionIds();
            List<QuestionDto> questions = questionFeignController.getQuestionsByQuestionIds(questionIds).getBody();
            quizResponses.add(QuizResponse.builder()
                            .id(quiz.getId())
                            .title(quiz.getTitle())
                            .questions(questions)
                            .build());
        }
        return quizResponses;
    }

    @Override
    public Integer calculateScore(List<AnswerResponse> answers) {
       return questionFeignController.getTotalScore(answers);
    }

    @Override
    public List<Integer> getQuestionIds(String category, Integer size) {
        return questionFeignController.getQuestionIds(category, size);
    }
}
