package com.ustc.software;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/2717:17
 */

import com.ustc.software.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SoftwareApplication.class)
public class UserSericeTest {
    @Autowired
    private UserService userService;
    @Test
    public void loginTest(){
        userService.login("gogogo", "123456", 50);
    }
}
