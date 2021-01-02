package amazing.community.mapper;

import amazing.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment (parent_id, reply, type, poster, gmt_create, gmt_modified, like_count, content) values (#{parent_id}, #{reply}, #{type}, #{poster}, #{gmt_create}, #{gmt_modified}, #{like_count}, #{content})")
    Integer insert(Comment comment);

    @Select("select * from comment where id = #{id}")
    Comment getById(@Param(value = "id") Integer id);

    @Select("select * from comment where parent_id=#{id} and type = 1 order by gmt_create desc")
    List<Comment> getCommentsByQuestionId(@Param(value = "id") Integer id);

    @Select("select * from comment where parent_id=#{id} and type = 2 order by gmt_create desc")
    List<Comment> getCommentsByCommentId(@Param(value = "id") Integer id);
}
