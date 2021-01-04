package amazing.community.enums;

public enum NotificationEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论"),
    Like_Question(3,"点赞了问题"),
    Like_COMMENT(4,"点赞了评论"),
    ;

    private Integer type;
    private String name;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    NotificationEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}
