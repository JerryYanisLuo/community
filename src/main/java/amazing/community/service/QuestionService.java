package amazing.community.service;

import amazing.community.dto.PaginationDTO;
import amazing.community.dto.QuestionDTO;
import amazing.community.exception.CustomizeErrorCodeImpl;
import amazing.community.exception.CustomizeException;
import amazing.community.mapper.QuestionMapper;
import amazing.community.mapper.SearchMapper;
import amazing.community.mapper.UserMapper;
import amazing.community.model.Question;
import amazing.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SearchMapper searchMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        if (page < 0 || page > (totalCount + size - 1) / size) page = 1;
        paginationDTO.setPagination(totalCount, page, size);

        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);
        return paginationDTO;
    }

    public PaginationDTO list(String id, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(id);
        if (page < 0 || page > (totalCount + size - 1) / size) page = 1;
        paginationDTO.setPagination(totalCount, page, size);

        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listByUserId(id, offset, size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOS);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCodeImpl.QUESTION_NOT_FOUND);
        }

        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {

        if (question.getId() == null) {
            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modified(question.getGmt_create());
            questionMapper.create(question);
        } else {
            question.setGmt_modified(System.currentTimeMillis());
            int e = questionMapper.update(question);
            if (e != 1) {
                throw new CustomizeException(CustomizeErrorCodeImpl.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(int id) {
        questionMapper.incView(id);
    }

    public List<Question> getReQuest(QuestionDTO questionDTO) {

        String tag = questionDTO.getTag();
        if(tag==null || tag.equals("")) return new ArrayList<>();
        String reg = Arrays.stream(tag.split("\\s+")).collect(Collectors.joining("|"));

        List<Question> questions = questionMapper.getRecQuest(reg, questionDTO.getId());
        return questions;
    }

    public PaginationDTO search(String name, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = searchMapper.count(name);
        if (page < 0 || page > (totalCount + size - 1) / size) page = 1;
        paginationDTO.setPagination(totalCount, page, size);

        Integer offset = size * (page - 1);
        List<Question> questions = searchMapper.searchQuestionByName(name, offset, size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);
        return paginationDTO;


    }

    public Integer findLatestQuest(User user) {

        return questionMapper.findLatestQuest(user);

    }

    public List<Question> hotList() {

        return questionMapper.hotList();

    }

    public void updateLikeCount(Integer targetId) {

        questionMapper.updateLikeCount(targetId);

    }
}
