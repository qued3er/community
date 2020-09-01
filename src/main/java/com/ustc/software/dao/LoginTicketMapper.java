package com.ustc.software.dao;

import com.ustc.software.entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1916:15
 */
@Mapper
@Repository
//通过注解去声明对应sql
public interface LoginTicketMapper {
    @Insert({"insert into login_ticket (user_id,ticket,status,expired) values(#{userId},#{ticket},#{status},#{expired})"})
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);
    //ticket是核心凭证 发送给客户端的是ticket  所以那ticket找[cookie]
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
//    @Select({"select id,user_id,ticket,status,expired from login_ticket where ticket=#{ticket}"})
    @ResultType(LoginTicket.class)
    LoginTicket selectByTicket(String ticket);
    //退出修改凭证状态
    @Update({"<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket}",
            "</script>"})
    int updateStatus(String ticket,int status);
}
