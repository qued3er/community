package com.ustc.software.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1716:55
 */
@Component
//通过JavaMailSender   ==》jar包  spring mail
public class MailClient {
    //打印日志
    private static final Logger logger= LoggerFactory.getLogger(MailClient.class);
    @Autowired
    private JavaMailSender mailSender;
    //发邮件条件： 发送者  接收者  标题  内容
    // 发送者固定[配置文件配置的]  其他变化
    //@Value用于注入静态值 减少get set  通过${}引用配置文件中的值
    @Value("${spring.mail.username}")
    private String from;
    //发送邮件功能封装  利用javamailsender
    public void sendMail(String to,String title,String content){
        //主要是构建MimeMessage  MimeMessage
        //MIME(Multipurpose Internet Mail Extensions)多用途互联网邮件扩展类型。
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            //可以接受发送html文件
            mimeMessageHelper.setText(content, true);
            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败"+e.getMessage());
        }
    }
}
