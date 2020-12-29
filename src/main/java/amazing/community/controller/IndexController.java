package amazing.community.controller;

import amazing.community.dto.QuestionDTO;
import amazing.community.mapper.QuestionMapper;
import amazing.community.mapper.UserMapper;
import amazing.community.model.Question;
import amazing.community.model.User;
import amazing.community.service.QuestionService;
import com.sun.jndi.cosnaming.CNCtx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


//允许这个类去接收前端的请求
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String Index(HttpServletRequest request,
                        Model model)
    {
        Cookie[] cookies = request.getCookies();
        if(cookies==null) return "index";

        for(Cookie cookie : cookies)
        {
            if(cookie.getName().equals("token"))
            {
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if(user!=null) {
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }

        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("questions", questionList);
        return "index";
    }

}
