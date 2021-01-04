package amazing.community.controller;

import amazing.community.dto.PaginationDTO;
import amazing.community.model.Question;
import amazing.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


//允许这个类去接收前端的请求
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String Index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page", defaultValue = "1") Integer page,
                        @RequestParam(name="size", defaultValue = "10") Integer size
                        )
    {
        PaginationDTO pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);
        model.addAttribute("state","0");

        List<Question> hotTopics = questionService.hotList();
        model.addAttribute("hotTopics", hotTopics);
        return "index";
    }

}
