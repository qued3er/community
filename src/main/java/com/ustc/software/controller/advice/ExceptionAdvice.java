package com.ustc.software.controller.advice;

import com.ustc.software.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/9/119:53
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    //无返回值  有很多参数 可以去spring查
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常" + e.getMessage());
        //区分请求 同步or异步请求？
        for (StackTraceElement stackTraceElement:
             e.getStackTrace()) {
            logger.error(stackTraceElement.toString());
        }
        String requestType = request.getHeader("X-Requested-With");
        //此时是异步请求
        if (requestType.equals("XMLHttpRequest")){
            response.setContentType("application/plain;charset=utf-8");
            //此时是异步请求返回JSON格式的字符串
            PrintWriter writer = response.getWriter();
            String errorMessage = CommunityUtil.getJSONString(1, "服务器异常");
            writer.write(errorMessage);
        }else {
            //同步请求的处理
            response.sendRedirect(request.getContextPath()+"/error");
        }

}
}
