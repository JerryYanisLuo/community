package amazing.community.dto;

import lombok.Data;

@Data
public class LikeDTO {

    private Integer targetId;
    private Integer type;//0:quest, 1:comment
    private Integer receiverId;

}
