package com.exam.quiz.feign;

import com.exam.quiz.dto.AnswerResponse;
import com.exam.quiz.dto.QuestionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "QUESTION/api/question/")
public interface QuestionFeignController {

    @GetMapping("questionId")
    List<Integer> getQuestionIds(@RequestParam String category, @RequestParam Integer size);

    @PostMapping("submit")
    Integer getTotalScore(List<AnswerResponse> answers);

    @GetMapping("getQuestions")
    ResponseEntity<List<QuestionDto>> getQuestionsByQuestionIds(@RequestParam List<Integer> questionIds);
}
