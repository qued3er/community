package com.ustc.software;

import com.ustc.software.dao.DiscussPostMapper;
import com.ustc.software.dao.UserMapper;
import com.ustc.software.entity.DiscussPost;
import com.ustc.software.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1715:04
 */
    @SpringBootTest
    @ContextConfiguration(classes = SoftwareApplication.class)
public class DiscussPostTest {
        @Autowired
        DiscussPostMapper discussPostMapper;
        @Test
        void contextLoads() {
            //测试通过
            int allRows = discussPostMapper.selectDiscussPostRows(149);
            System.out.println(allRows);
            List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPost(0, 1, 1);
            for (DiscussPost post :
                    discussPosts) {
                System.out.println(discussPosts);
            }
        }
    }
