package amazing.community.dto;

import amazing.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Integer id; //通知id
    private Long gmt_create;
    private Integer status;
    private User notifier; //写回复的人
    private Integer main_id; //所属于的话题
    private Integer parent_id; //回复的东西id
    private Integer type;
    private String main_title;
}
