package com.ustc.software.controller.interceptor;

import com.ustc.software.annotation.LoginRequired;
import com.ustc.software.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/2814:10
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    //去排除静态资源提高效率
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Object是拦截的目标  ===》当是方法时才拦截 handlerMethod 与method
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);
            //该方法需要登录才访问 但是又没登录
            if (annotation!=null&&hostHolder.getUser()==null){
                //去登录
                response.sendRedirect(request.getContextPath()+"/login");
                //拒绝后续的请求
                return false;
            }
        }
        return true;
    }
}
