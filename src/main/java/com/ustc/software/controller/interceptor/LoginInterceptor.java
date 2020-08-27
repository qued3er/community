package com.ustc.software.controller.interceptor;

import com.ustc.software.entity.LoginTicket;
import com.ustc.software.entity.User;
import com.ustc.software.service.UserService;
import com.ustc.software.util.CookieUtil;
import com.ustc.software.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/2620:19
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    //controller之前通过cookie得到ticket  再通过ticket拿到user
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        //登陆了
        if (ticket!=null){
            //根据ticketId去查loginTicket
            LoginTicket loginTicket=userService.findLoginTicketById(ticket);
            //凭证是否有效？  0 仍有效
            if (loginTicket!=null&&loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
                User curUser = userService.findUserById(loginTicket.getUserId());
                //当前用户的状态需要缓存 给之后用【服务器是多线程服务  要考虑多线程】====》通过ThreadLocal解决
                //隔离解决。
                hostHolder.setUser(curUser);
            }
        }
        return false;
    }
    //controller之后执行 需要去执行 把user放到modelandView中去 拿给模板引擎渲染
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser", user);
        }
    }

    //模板引擎渲染完后 执行remove  否则会占用内存 一直增加

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.removeUser();
    }
}
