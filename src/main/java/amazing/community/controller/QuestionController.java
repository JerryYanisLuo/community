package amazing.community.controller;

import amazing.community.dto.CommentDTO;
import amazing.community.dto.QuestionDTO;
import amazing.community.model.Question;
import amazing.community.model.User;
import amazing.community.service.CommentService;
import amazing.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    private String question(@PathVariable(name = "id") Integer id,
                            Model model,
                            HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        String name = user==null?"guestView":("view"+user.getId());
        HashSet<Integer> view = (HashSet<Integer>) request.getSession().getAttribute(name);
        if(view==null || !view.contains(id))
        {
            questionService.incView(id);//累加浏览数
            if(view==null) view = new HashSet<>();
            view.add(id);
            request.getSession().setAttribute(name, view);
        }

        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("question", questionDTO);

        List<CommentDTO> comments = commentService.listByQuestionId(id);
        model.addAttribute("comments", comments);

        List<Question> reQuest = questionService.getReQuest(questionDTO);
        model.addAttribute("reQuest",reQuest);
        return "question";
    }


}
