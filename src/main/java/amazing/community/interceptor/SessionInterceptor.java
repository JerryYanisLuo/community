package amazing.community.interceptor;

import amazing.community.mapper.NotificationMapper;
import amazing.community.mapper.UserMapper;
import amazing.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        if(cookies==null) return true;
        for(Cookie cookie : cookies)
        {
            if(cookie.getName().equals("token"))
            {
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if(user!=null) {
                    request.getSession().setAttribute("user", user);

                    int messageCount = notificationMapper.countMessage(user);
                    request.getSession().setAttribute("messageCount",messageCount);

                }
                break;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
