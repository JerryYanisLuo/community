package amazing.community.controller;

import amazing.community.dto.LikeDTO;
import amazing.community.dto.ResultDTO;
import amazing.community.enums.LikeTypeEnum;
import amazing.community.enums.NotificationEnum;
import amazing.community.exception.CustomizeErrorCodeImpl;
import amazing.community.model.User;
import amazing.community.service.CommentService;
import amazing.community.service.NotificationService;
import amazing.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@Controller
public class LikeController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @ResponseBody
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public Object like(@RequestBody LikeDTO likeDTO,
                       HttpServletRequest request)
    {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCodeImpl.NO_LOGIN);
        }

        if(likeDTO.getReceiverId()!=user.getId()) {
            String name = "like" + user.getId();
            HashSet<String> like = (HashSet<String>) request.getSession().getAttribute(name);
            if (like == null || !like.contains(likeDTO.getType() + "%" + likeDTO.getTargetId())) {

                if (like == null) like = new HashSet<>();
                like.add(likeDTO.getType() + "%" + likeDTO.getTargetId());
                request.getSession().setAttribute(name, like);

                if (likeDTO.getType() == LikeTypeEnum.COMMENT.getType()) {
                    commentService.updateLikeCount(likeDTO.getTargetId());
                } else if (likeDTO.getType() == LikeTypeEnum.QUESTION.getType()) {
                    questionService.updateLikeCount(likeDTO.getTargetId());
                    notificationService.notifyLike(user.getId(), likeDTO.getReceiverId(), likeDTO.getTargetId(), NotificationEnum.Like_Question.getType());
                }
            }
        }

        return ResultDTO.okOf();
    }
}
