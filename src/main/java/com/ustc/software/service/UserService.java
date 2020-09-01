package com.ustc.software.service;

import com.ustc.software.dao.LoginTicketMapper;
import com.ustc.software.dao.UserMapper;
import com.ustc.software.entity.LoginTicket;
import com.ustc.software.entity.User;
import com.ustc.software.util.CommunityConstant;
import com.ustc.software.util.CommunityUtil;
import com.ustc.software.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1616:50
 */
@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    //注册发邮件
    @Autowired
    private MailClient mailClient;
    //注入模板引擎
    @Autowired
    private TemplateEngine templateEngine;
    //
    @Value("${community.path.domain}")
    private String domain;
    //项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    public User findUserById(int userId){
        return userMapper.findUserById(userId);
    }
    //注册方法 返回map 因为要返回注册的各种结果信息 ..
    //
    public Map<String,Object>register(User user){
        Map<String,Object> res=new HashMap<>();
        //空值处理
        if (user==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            res.put("usernameMessage", "用户名不能为空");
            return res;
        }
        if (StringUtils.isBlank(user.getPassword())){
            res.put("passwordMessage", "密码不能为空");
            return res;
        }
        if (StringUtils.isBlank(user.getEmail())){
            res.put("emailMessage", "邮箱不能为空");
            return res;
        }

        //验证账号是否已经存在
        User ok = userMapper.findUserByName(user.getUsername());
        if (ok!=null){
            res.put("usernameMessage","账号已存在！");
            return res;
        }
        //验证邮箱
        ok = userMapper.findUserByEmail(user.getUsername());
        if (ok!=null){
            res.put("emailMessage","邮箱已被注册！");
            return res;
        }
        //真正的注册用户的逻辑
        user.setSalt(CommunityUtil.getRandomString().substring(0, 6));
        //加密后的覆盖未加密的
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        //未激活
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.getRandomString());
        user.setCreateTime(new Date());
        //初始化随机头像  登录后可重上传
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userMapper.insertUser(user);

        //插入后 从数据库获得 才有id
        //发送激活邮件[模板是activation.html]
        Context context=new Context();
        //给模板传值
        context.setVariable("email",user.getEmail());
        //自己定的url的格式
        //community.path.domain=http://localhost:8080+server.servlet.context-path=/community + /activation/101/code
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url", url);
        String content=templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "请激活您的账号", content);
        //map为空  没问题
        return res;
    }
    //处理激活逻辑  实现了CommunityConstant
    //连接中的请求有id与激活码  依次来判断是否能激活成功
    //status是1 已经激活过
    //激活码错误
    //激活成功改状态
    public int activation(int id,String code){
        User user = userMapper.findUserById(id);
        if (user.getStatus()==1){
            return ACTIVATION_REPEAT;
        } else if (code.equals(user.getActivationCode())){
            userMapper.updateUserStatus(id, 1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAIL;
        }
    }
    //实现登录的逻辑
    //返回map时因为可能会有多种情况要返回  【成功 失败====》失败还有多重情况】
    //password是明文
    public Map<String,Object> login(String username,String password,int expiredSeconds){
        Map<String,Object> res=new HashMap<>();
        //空值处理
        if (StringUtils.isBlank(username)){
            res.put("usernameMessage", "用户名不能为空");
            return res;
        }
        if (StringUtils.isBlank(password)){
            res.put("passwordMessage", "密码不能为空");
            return res;
        }
        //验证账号
        User user = userMapper.findUserByName(username);
        if (user==null){
            res.put("usernameMessage", "账号不存在");
            return res;
        }
        if (user.getStatus()==0){
            res.put("usernameMessage", "该账号未激活");
            return res;
        }
        String encry = CommunityUtil.md5(password + user.getSalt());
        //使用==判断 字符串的都是傻逼
        if (!user.getPassword().equals(encry)){
            res.put("passwordMessage", "密码错误");
            return res;
        }
        //否则就是通过给他生成凭证
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setTicket(CommunityUtil.getRandomString());
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        res.put("ticket", loginTicket.getTicket());
        return res;
    }
    //退出把凭证置为无效即可
    public void loginOut(String ticket){
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicketById(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    //更新user头像路径  真正实现存储在表现层 因为multipartFile是MVC的对象
    public int updateHeader(int userId,String headerUrl){
        return userMapper.updateUserHeader(userId, headerUrl);
    }

    //更新password
    public int updatePassword(int id,String password){
        return userMapper.updateUserHeader(id, password);
    }
}
