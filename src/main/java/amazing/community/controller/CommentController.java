package amazing.community.controller;

import amazing.community.dto.CommentCreateDTO;
import amazing.community.dto.ResultDTO;
import amazing.community.exception.CustomizeErrorCodeImpl;
import amazing.community.model.Comment;
import amazing.community.model.User;
import amazing.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object postFirstLevelComment(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCodeImpl.NO_LOGIN);
        }

        if(commentCreateDTO == null || commentCreateDTO.getContent()==null || commentCreateDTO.getContent().equals(""))
        {
            return ResultDTO.errorOf(CustomizeErrorCodeImpl.COMMENT_EMPTY);
        }

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentCreateDTO, comment);
        comment.setGmt_create(System.currentTimeMillis());
        comment.setGmt_modified(comment.getGmt_create());
        comment.setPoster(user.getId());
        comment.setLike_count(0);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

}
