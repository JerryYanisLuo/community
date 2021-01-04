package amazing.community.mapper;

import amazing.community.model.Question;
import amazing.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag) values (#{title}, #{description}, #{gmt_create}, #{gmt_modified}, #{creator}, #{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator in (select id from user where account_id = ${account_id}) limit #{offset}, #{size}")
    List<Question> listByUserId(@Param(value = "account_id") String account_id, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator in (select id from user where account_id = ${account_id})")
    Integer countByUserId(@Param(value = "account_id") String account_id);

    @Select("select * from question where id = #{id}")
    Question getById(Integer id);

    @Update("update question set title=#{title}, description=#{description}, gmt_modified=#{gmt_modified}, tag=#{tag} where id = #{id}")
    Integer update(Question question);

    @Update("update question set view_count = view_count+1 where id=#{id}")
    Integer incView(@Param(value = "id") Integer id);

    @Update("update question set comment_count=comment_count+1 where id=#{id}")
    void incComm(Question question);

    @Select("select * from question where tag regexp #{r} and id!=#{id}")
    List<Question> getRecQuest(@Param(value = "r") String reg, @Param(value = "id") Integer id);

    @Select("select id from question where creator = #{id} order by gmt_create desc limit 0,1")
    Integer findLatestQuest(User user);

    @Select("select * from question order by (comment_count+like_count+view_count) desc limit 0,10")
    List<Question> hotList();

    @Update("update question set like_count = like_count+1 where id = #{id}")
    void updateLikeCount(@Param(value = "id") Integer targetId);
}
