package amazing.community.enums;

public enum LikeTypeEnum {

    QUESTION(0),
    COMMENT(1);

    private Integer type;

    LikeTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
