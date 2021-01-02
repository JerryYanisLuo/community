package amazing.community.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Integer parent_id;
    private Integer reply;
    private String content;
    private Integer type;
}
