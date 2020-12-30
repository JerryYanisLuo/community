package amazing.community.controller;

import amazing.community.dto.PaginationDTO;
import amazing.community.mapper.QuestionMapper;
import amazing.community.model.User;
import amazing.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action", value = "") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name="page", defaultValue = "1") Integer page,
                          @RequestParam(name="size", defaultValue = "5") Integer size)
    {
        if("question".equals(action))
        {
            model.addAttribute("section", "question");
            model.addAttribute("sectionName", "我的提问");
        }
        else if("reply".equals(action))
        {
            model.addAttribute("section", "reply");
            model.addAttribute("sectionName", "最新回复");
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        PaginationDTO pagination = questionService.list(user.getAccount_id(), page, size);
        model.addAttribute("pagination", pagination);

        return "profile";
    }

}
