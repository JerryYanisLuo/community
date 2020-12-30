package amazing.community.service;

import amazing.community.mapper.UserMapper;
import amazing.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {

        User dbUser = userMapper.findByUserAccountId(user.getAccount_id());
        if(dbUser==null)
        {
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            userMapper.insert(user);
        }
        else
        {
            dbUser.setGmt_modified(System.currentTimeMillis());
            dbUser.setAccount_id(user.getAvatar_url());
            dbUser.setName(user.getName());
            dbUser.setBio(user.getBio());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }


    }
}
