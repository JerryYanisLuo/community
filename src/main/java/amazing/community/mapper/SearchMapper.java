package amazing.community.mapper;

import amazing.community.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SearchMapper {

    List<Question> searchQuestionByName(String name, Integer offset, Integer size);

    Integer count(String name);
}
