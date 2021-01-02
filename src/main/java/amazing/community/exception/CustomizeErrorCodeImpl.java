package amazing.community.exception;

public enum CustomizeErrorCodeImpl implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"您想要访问的话题不在啦！！！换个话题试试吧~"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"未能登录，请先登录"),
    SYS_ERR(2004,"抱歉>_<服务崩溃啦！！！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误"),
    COMMENT_NOT_FOUND(2006,"评论不存在"),
    COMMENT_EMPTY(2007,"评论不能为空");


    private String message;
    private Integer code;

    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCodeImpl(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
