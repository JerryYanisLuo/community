package amazing.community.advice;

import amazing.community.dto.ResultDTO;
import amazing.community.exception.CustomizeErrorCodeImpl;
import amazing.community.exception.CustomizeException;
import com.alibaba.fastjson.JSON;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e,
                  Model model,
                  HttpServletRequest request,
                  HttpServletResponse response) throws IOException {

        String contentType = request.getContentType();
        if (contentType.equals("application/json")) {
            ResultDTO resultDTO;
            //返回json
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCodeImpl.SYS_ERR);
            }

            response.setContentType("application/json");
            response.setStatus(200);
            response.setCharacterEncoding("utf-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSON.toJSONString(resultDTO));
            printWriter.close();
            return null;

        } else {
            //做错误跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCodeImpl.SYS_ERR);
            }
            return new ModelAndView("error");
        }
    }
}
