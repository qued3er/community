package com.ustc.software;

import com.ustc.software.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1717:37
 */
@SpringBootTest
@ContextConfiguration(classes = SoftwareApplication.class)
public class MailTest {
    @Autowired
    MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    void contextLoads() {
        mailClient.sendMail("879920484@qq.com", "test", "gogogo");
    }
    @Test
    void testHtml() {
        Context context=new Context();
        //传给模板引擎的参数
        context.setVariable("name","刘亚芳");

        String content = templateEngine.process("/mail/demo", context);
        mailClient.sendMail("879920484@qq.com","来自李帅奇的表白",content);
    }
}
