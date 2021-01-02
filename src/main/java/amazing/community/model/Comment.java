package amazing.community.model;

import lombok.Data;

@Data
public class Comment {
    private Integer id;
    private Integer parent_id;
    private Integer reply;
    private Integer type;
    private Integer poster;
    private Long gmt_create;
    private Long gmt_modified;
    private int like_count;
    private String content;

}
