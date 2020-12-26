package amazing.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


//允许这个类去接收前端的请求
@Controller
public class HelloController {

    @GetMapping("/hello")
    public String Hello(@RequestParam(name = "name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        return "hello";
    }

}
