package com.exam.quiz.controller;

import com.exam.quiz.dto.AnswerResponse;
import com.exam.quiz.dto.QuizResponse;
import com.exam.quiz.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/quiz/")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String title, @RequestParam String category, @RequestParam Integer size){
        try {
            String message = quizService.createQuiz(title, category, size);
            return ResponseEntity.ok(message);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{title}")
    public ResponseEntity<List<QuizResponse>> getQuiz(@PathVariable String title){
        try {
            List<QuizResponse>  quizzes =
                    quizService.getQuizByTitle(title);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswers(@RequestBody List<AnswerResponse> answers){
        try {
            Integer score = quizService.calculateScore(answers);
            return ResponseEntity.ok(score);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("questionId")
    public ResponseEntity<?> getQuestions(@RequestParam String category, @RequestParam Integer size){
        try {
            List<Integer> questionIds = quizService.getQuestionIds(category, size);
            return ResponseEntity.ok(questionIds);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("home")
    @PreAuthorize("hasRole('user')")
//    @RolesAllowed("user")
    public ResponseEntity<?> home(){
        return ResponseEntity.ok("home");
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> adminPage(){
        return ResponseEntity.ok("Admin page");
    }
}
