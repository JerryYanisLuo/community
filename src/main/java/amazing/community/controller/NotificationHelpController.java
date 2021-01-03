package amazing.community.controller;

import amazing.community.model.User;
import amazing.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationHelpController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notify/{infos}")
    public String readMessage(@PathVariable(name = "infos") String infos, //notification id
                              HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return "redirect:/";

        String[] info = infos.split("&");
        int id = Integer.parseInt(info[0]);
        String quest = info[1];
        notificationService.read(id);
        request.getSession().setAttribute("messageCount", (int) request.getSession().getAttribute("messageCount") - 1);
        return "redirect:/question/" + quest;
    }

}
