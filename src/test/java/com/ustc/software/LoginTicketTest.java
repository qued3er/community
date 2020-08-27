package com.ustc.software;

import com.ustc.software.dao.LoginTicketMapper;
import com.ustc.software.entity.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1916:54
 */
@SpringBootTest
@ContextConfiguration(classes = SoftwareApplication.class)
public class LoginTicketTest {
    @Autowired
    LoginTicketMapper loginTicketMapper;
    @Test
    void contextLoads() {
//        LoginTicket loginTicket=new LoginTicket();
//        loginTicket.setId(1);
//        loginTicket.setExpired(new Date());
//        loginTicket.setStatus(1);
//        loginTicket.setUserId(10);
//        loginTicket.setTicket("ase213");
//        loginTicketMapper.insertLoginTicket(loginTicket);
       loginTicketMapper.updateStatus("ase213",0);
    }
}
