package amazing.community.service;

import amazing.community.dto.NotificationDTO;
import amazing.community.enums.NotificationEnum;
import amazing.community.enums.NotificationStatusEnum;
import amazing.community.mapper.CommentMapper;
import amazing.community.mapper.NotificationMapper;
import amazing.community.mapper.QuestionMapper;
import amazing.community.mapper.UserMapper;
import amazing.community.model.Comment;
import amazing.community.model.Notification;
import amazing.community.model.Question;
import amazing.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 要找到所有未读的评论当前用户话题的消息
     *
     * @param id 当前用户的id
     * @return
     */
    public List<NotificationDTO> grabNotifications(int id, int type, int status) {

        List<Notification> notifications = notificationMapper.findMessage(id, status, type);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        if (notifications == null || notifications.size() == 0) return notificationDTOS;

        for (Notification notification : notifications) {
            if (notification.getNotifier() == notification.getReceiver()) continue;
            NotificationDTO notificationDTO = new NotificationDTO();
            User notifier = userMapper.findById(notification.getNotifier());
            notificationDTO.setNotifier(notifier);
            notificationDTO.setGmt_create(notification.getGmt_create());
            notificationDTO.setId(notification.getId());
            notificationDTO.setStatus(notification.getStatus());
            notificationDTO.setType(notification.getType());
            notificationDTO.setParent_id(notification.getOuter_id());
            notificationDTO.setMain_title("");

            //main-id
            if (type == NotificationEnum.REPLY_QUESTION.getType() || type==NotificationEnum.Like_Question.getType()) {
                //reQuest likeQuest
                notificationDTO.setMain_id(notification.getOuter_id());
                Question question = questionMapper.getById(notification.getOuter_id());
                notificationDTO.setMain_title(question.getTitle());
                notificationDTO.setMain_id(question.getId());
            } else if (type == NotificationEnum.REPLY_COMMENT.getType()) {
                //reComm
                Comment fromComment = commentMapper.getById(notification.getOuter_id());
                Comment mainComment = null;
                if (fromComment.getType() == 1) {
                    //main comment
                    mainComment = fromComment;
                } else {
                    mainComment = commentMapper.getById(fromComment.getParent_id());
                }
                Question question = questionMapper.getById(mainComment.getParent_id());
                notificationDTO.setMain_id(question.getId());
            }
            notificationDTOS.add(notificationDTO);
        }

        return notificationDTOS;
    }

    public void read(Integer id) {
        notificationMapper.updateNotify(id);
    }

    public void notifyLike(int id, Integer receiverId, Integer targetId, Integer type) {

        Notification notification = new Notification();
        notification.setNotifier(id);
        notification.setReceiver(receiverId);
        notification.setOuter_id(targetId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setType(type);
        notification.setGmt_create(System.currentTimeMillis());
        notificationMapper.insert(notification);

    }
}
