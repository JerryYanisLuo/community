package amazing.community.model;

import lombok.Data;

@Data
public class Notification {

    private Integer id;
    private Integer notifier;
    private Integer receiver;
    private Integer outer_id;
    private Integer type;
    private Long gmt_create;
    private Integer status;

}
