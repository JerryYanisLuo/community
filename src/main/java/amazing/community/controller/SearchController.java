package amazing.community.controller;

import amazing.community.dto.PaginationDTO;
import amazing.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "10") Integer size) {

        PaginationDTO pagination = questionService.search(name, page, size);
        model.addAttribute("pagination", pagination);
        model.addAttribute("state","0");
        return "index";
    }


}
