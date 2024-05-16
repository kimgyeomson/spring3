package com.site.sbb.question;

import com.site.sbb.answer.AnswerForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) {
        Page<Question> paping = this.questionService.getList(page);
        model.addAttribute("paging", paping);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) { // 오류가 있는 경우 다시 제목과 내용을 작성하는 화면으로 돌아감
            return "question_form";
        }
        // 오류가 없을 경우에만 질문이 등록되도록 만들어짐
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }

//    @PostMapping("/create")
//    public String questionCreate(@RequestParam(value="subject") String subject,
//                                 @RequestParam(value="content") String content) {
//        this.questionService.create(subject, content);
//        return "redirect:/question/list";
//    }
}
