package amazing.community.service;

import amazing.community.dto.CommentDTO;
import amazing.community.enums.CommentTypeEnum;
import amazing.community.exception.CustomizeErrorCodeImpl;
import amazing.community.exception.CustomizeException;
import amazing.community.mapper.CommentMapper;
import amazing.community.mapper.QuestionMapper;
import amazing.community.mapper.UserMapper;
import amazing.community.model.Comment;
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

    @Transactional
    public void insert(Comment comment) {

        if (comment.getParent_id() == null || comment.getParent_id() == 0) {
            throw new CustomizeException(CustomizeErrorCodeImpl.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment parent = commentMapper.getById(comment.getParent_id());
            if (parent == null) {
                throw new CustomizeException(CustomizeErrorCodeImpl.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        } else if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            //回复问题
            Question question = questionMapper.getById(comment.getParent_id());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCodeImpl.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.incComm(question);
        } else {
            //评论类型错误
            throw new CustomizeException(CustomizeErrorCodeImpl.TYPE_PARAM_WRONG);
        }
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
                if(ch.getReply()!=null || ch.getReply()==ch.getParent_id())
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
}
