package amazing.community.dto;

import amazing.community.model.User;
import lombok.Data;

import java.util.List;

@Data
public class CommentDTO {

    private Integer id;
    private Integer parent_id;
    private Integer reply;
    private Integer type;
    private Integer poster;
    private Long gmt_create;
    private Long gmt_modified;
    private Integer like_count;
    private String content;
    private User user;
    private User parent;
    private List<CommentDTO> comments;

}
