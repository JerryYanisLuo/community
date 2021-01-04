package amazing.community.service;

import amazing.community.dto.CommentDTO;
import amazing.community.enums.CommentTypeEnum;
import amazing.community.enums.NotificationEnum;
import amazing.community.enums.NotificationStatusEnum;
import amazing.community.exception.CustomizeErrorCodeImpl;
import amazing.community.exception.CustomizeException;
import amazing.community.mapper.CommentMapper;
import amazing.community.mapper.NotificationMapper;
import amazing.community.mapper.QuestionMapper;
import amazing.community.mapper.UserMapper;
import amazing.community.model.Comment;
import amazing.community.model.Notification;
import amazing.community.model.Question;
import amazing.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment) {

        if (comment.getParent_id() == null || comment.getParent_id() == 0) {
            throw new CustomizeException(CustomizeErrorCodeImpl.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment parent = commentMapper.getById(comment.getReply());
            if (parent == null) {
                throw new CustomizeException(CustomizeErrorCodeImpl.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

            if(comment.getPoster()!=parent.getPoster())
            {
                Notification notification = createNotify(comment, parent.getPoster(), NotificationEnum.REPLY_COMMENT.getType());
                notificationMapper.insert(notification);
            }

        } else if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            //回复问题
            Question question = questionMapper.getById(comment.getParent_id());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCodeImpl.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.incComm(question);

            if(comment.getPoster()!=question.getCreator())
            {
                Notification notification = createNotify(comment, question.getCreator(), NotificationEnum.REPLY_QUESTION.getType());
                notificationMapper.insert(notification);
            }

        } else {
            //评论类型错误
            throw new CustomizeException(CustomizeErrorCodeImpl.TYPE_PARAM_WRONG);
        }
    }

    private Notification createNotify(Comment comment, Integer receiver, Integer type) {
        Notification notification = new Notification();
        notification.setGmt_create(System.currentTimeMillis());
        notification.setType(type);
        notification.setOuter_id(comment.getParent_id());
        notification.setNotifier(comment.getPoster());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        return notification;
    }


    public List<CommentDTO> listByQuestionId(Integer id) {

        List<Comment> root = commentMapper.getCommentsByQuestionId(id);
        List<CommentDTO> comments = new ArrayList<>();
        for(Comment comment : root)
        {
            CommentDTO commentDTO = new CommentDTO();
            User user = userMapper.findById(comment.getPoster());
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            commentDTO.setComments(new ArrayList<>());

            List<Comment> children = commentMapper.getCommentsByCommentId(comment.getId());
            for(Comment ch : children)
            {
                CommentDTO cDTO = new CommentDTO();
                User u = userMapper.findById(ch.getPoster());
                User p = null;
                BeanUtils.copyProperties(ch,cDTO);
                cDTO.setUser(u);
                if(ch.getReply()!=null && ch.getReply()!=ch.getParent_id())
                {
                    Comment c = commentMapper.getById(ch.getReply());
                    p = userMapper.findById(c.getPoster());
                }
                cDTO.setParent(p);
                commentDTO.getComments().add(cDTO);
            }
            comments.add(commentDTO);
        }
        return comments;
    }

    public void updateLikeCount(Integer comm) {

        commentMapper.updateLikeCount(comm);

    }
}
