package amazing.community.mapper;

import amazing.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into user(name, account_id, token, gmt_create, gmt_modified, bio, avatar_url) values (#{name},#{account_id},#{token},#{gmt_create},#{gmt_modified},#{bio},#{avatar_url})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer creator);

    @Select("select * from user where account_id = #{account_id} limit 0,1")
    User findByUserAccountId(@Param("account_id") String account_id);

    @Update("update user set name=#{name}, token=#{token}, gmt_modified=#{gmt_modified}, bio=#{bio}, avatar_url=#{avatar_url} where id = #{id}")
    void update(User user);
}
