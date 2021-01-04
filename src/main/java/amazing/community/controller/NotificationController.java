package amazing.community.controller;


import amazing.community.dto.NotificationDTO;
import amazing.community.enums.NotificationEnum;
import amazing.community.enums.NotificationStatusEnum;
import amazing.community.model.User;
import amazing.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{action}")
    public String notification(@PathVariable(name = "action", value = "") String action,
                               Model model,
                               HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        List<NotificationDTO> notifyQuest = notificationService.grabNotifications(user.getId(), NotificationEnum.REPLY_QUESTION.getType(), NotificationStatusEnum.UNREAD.getStatus());
        List<NotificationDTO> notifyComm = notificationService.grabNotifications(user.getId(), NotificationEnum.REPLY_COMMENT.getType(), NotificationStatusEnum.UNREAD.getStatus());
        List<NotificationDTO> notifyLikeQuest = notificationService.grabNotifications(user.getId(), NotificationEnum.Like_Question.getType(), NotificationStatusEnum.UNREAD.getStatus());
        model.addAttribute("questNum",notifyQuest.size());
        model.addAttribute("commNum",notifyComm.size());
        model.addAttribute("likeQuestNum",notifyLikeQuest.size());

        if ("replyQuest".equals(action)) {
            //评论了我的话题
            model.addAttribute("section", "replyQuest");
            model.addAttribute("sectionName", "评论我的");
            model.addAttribute("reNotifications", notifyQuest);

        } else if ("replyComm".equals(action)) {
            model.addAttribute("section", "replyComm");
            model.addAttribute("sectionName", "@我的");
            model.addAttribute("reNotifications", notifyComm);
        }
        else if("likeQuest".equals(action))
        {
            model.addAttribute("section", "likeQuest");
            model.addAttribute("sectionName", "点赞我的");
            model.addAttribute("reNotifications", notifyLikeQuest);
        }
        return "notification";
    }


}
