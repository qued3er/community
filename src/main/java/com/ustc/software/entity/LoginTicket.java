package com.ustc.software.entity;

import java.util.Date;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1916:12
 */
//登录成功后 服务端给客户端发布的登录凭证
public class LoginTicket {
    private int id;
    private int userId;
    //不重复随机字符串
    private String ticket;
    //凭证是否有效  0有1无
    private int status;
    //过期时间
    private Date expired;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }
}
