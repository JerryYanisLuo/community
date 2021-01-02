package amazing.community.mapper;

import amazing.community.model.Notification;
import amazing.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("insert into notification (notifier, receiver, outer_id, type, gmt_create, status) values (#{notifier}, #{receiver}, #{outer_id}, #{type}, #{gmt_create}, #{status})")
    Integer insert(Notification notification);

    @Select("select * from notification where receiver=#{id} and status=#{status} and type = #{type} and notifier!=#{id}")
    List<Notification> findMessage(@Param(value = "id") int id, @Param(value = "status") int status, @Param(value = "type") int type);

    @Update("update notification set status=1 where id = #{id}")
    void updateNotify(@Param(value = "id") Integer id);

    @Select("select * from notification where id = #{id}")
    Notification findMessageById(Integer id);

    @Select("select count(1) from notification where receiver=#{id} and status=0")
    int countMessage(User user);
}
