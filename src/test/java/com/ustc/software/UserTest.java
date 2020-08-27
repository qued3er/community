package com.ustc.software;

import com.ustc.software.dao.UserMapper;
import com.ustc.software.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1714:38
 */
@SpringBootTest
@ContextConfiguration(classes = SoftwareApplication.class)
public class UserTest {
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        //测试通过
        User user=userMapper.findUserById(1);
        System.out.println(user);
    }
}
