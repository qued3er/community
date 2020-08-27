package com.ustc.software.util;

import com.ustc.software.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author li
 * @Title:
 * @Description:持有用户的信息 用于代替session  [线程隔离]
 * @date 2020/8/2620:36
 */

@Component
public class HostHolder {
    /*set 往里边存值   根据线程来存的  每个线程map对象不一样
    * 需要深入了解ThreadLocal*/
    private ThreadLocal<User> users=new ThreadLocal();

    public  void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return  users.get();
    }

    /*  清理users 在当前服务结束的时候清理[即模板引擎渲染完 返回之后]   即user信息在内存中仅存储一个request服务 */
    public void removeUser(){
        users.remove();
    }
}
