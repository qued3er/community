package com.ustc.software.controller;

import com.google.code.kaptcha.Producer;
import com.ustc.software.entity.User;
import com.ustc.software.service.UserService;
import com.ustc.software.util.CommunityConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1718:57
 */
@Api("登录逻辑的实现")
@Controller
public class LoginController implements CommunityConstant {
    private Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private String context;
    @ApiOperation("访问注册页面")
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }
    @ApiOperation("具体注册逻辑的实现")
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(@ApiIgnore Model model,@ApiParam("注册的user") User user){
        Map<String, Object> ok = userService.register(user);
        //注册成功
        if (ok==null||ok.isEmpty()){
            //跳转模板主要需要的两个信息  1是提示信息  2是跳转到哪儿
            model.addAttribute("msg", "请到邮箱激活账号");
//            到首页  等激活
            model.addAttribute("url", "/index");
            //返回到跳转页面
            return "/site/operate-result";
        }
        //失败时 返回原页面
        else {
            model.addAttribute("usernameMessage",ok.get("usernameMessage"));
            model.addAttribute("passwordMessage",ok.get("passwordMessage"));
            model.addAttribute("emailMessage",ok.get("emailMessage"));
            //根据注册的账号 密码 邮箱进行错误提示
            model.addAttribute("user", user);
            return "/site/register";
        }
    }
    //community.path.domain=http://localhost:8080+server.servlet.context-path=/community + /activation/101/code
//    String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
    @ApiOperation("激活userId用户，激活码为code")
    //如何从请求路径中获取信息？？
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    //从请求路径中取参数
    //复用跳转页面
    //成功到登录页面
    //失败回首页
    /**
     * 什么时候是 请求什么时候是路径   这return是请求还是路径*/
    public String activation(Model model,
                             @ApiParam("激活的userId") @PathVariable("userId") int userId,
                             @ApiParam("激活码") @PathVariable("code") String code){
        int result = userService.activation(userId, code);
        if (result==ACTIVATION_SUCCESS){
            model.addAttribute("msg", "激活成功，即将转向登录页面");
            model.addAttribute("url", "/login");
        }else if (result==ACTIVATION_REPEAT){
            model.addAttribute("msg", "重复激活");
            model.addAttribute("url", "/index");
        }else {
            model.addAttribute("msg","激活码错误");
            model.addAttribute("url", "/index");
        }
        return "/site/operate-result";
    }
    @ApiOperation("返回登录页面")
    //登录页面
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String login(){
        return "/site/login";
    }
    @ApiOperation("生成验证码")
    //获取验证码图片 login界面的一个验证码部分的一个请求
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    //返回的是图片 通过response 手动向浏览器输出
    //请求login界面  服务端会生成验证码    然后点击登录请求时 会携带验证码 。
    // 所以需要session记住浏览器与对应的验证码[跨请求]
    public void getKaptcha(@ApiIgnore HttpServletResponse response, @ApiIgnore HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        //把text放入session  之后做对比
        session.setAttribute("kaptcha", text);
        //把图片传给浏览器端
        response.setContentType("image/png");
        ServletOutputStream outputStream = null;
        try {
            //获取response的输出流  由于是图片 字节流
            outputStream = response.getOutputStream();
            //输入输出图片的工具===》imageIO
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @ApiOperation("登录请求 方式post")
    //与上边login请求方式不同
    //用户请求页面的时候  生成的验证码放入了session中，需要在这拿出做对比是否验证码正确
    //要把生成的ticket放到response中返回给浏览器cookie。
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(@ApiParam("用户名") String username,
                        @ApiParam("密码") String password,
                        @ApiParam("验证码") String code,
                        @ApiParam("是否记住我") boolean rememberMe,Model model,
                        @ApiParam("session是为了验证" +
                                "因为验证码和登录时不同的请求") @ApiIgnore HttpSession session,
                        @ApiParam("生成登录凭证放到response") @ApiIgnore HttpServletResponse response){
        //判断验证码
        String kaptcha= (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(code)||StringUtils.isBlank(kaptcha)||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("kaptchaMessage", "验证码错误");
            return "/site/login";
        }
        //账号密码==》有没有rememberme
        int expiredSeconds=rememberMe?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> res = userService.login(username, password, expiredSeconds);
        //成功含ticket==>返回首页
        if (res.containsKey("ticket")){
            //生成一个cookie塞到response中去
            Cookie cookie=new Cookie("ticket", res.get("ticket").toString());
            //请求携带cookie的路径
            cookie.setPath(context);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            //登录失败返回的错误信息
            res.put("usernameMessage", res.get("usernameMessage"));
            res.put("passwordMessage", res.get("passwordMessage"));
            return "/site/login";
        }
    }
    @ApiOperation("退出登录")
    //获取请求头中的cookie把他置无效
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logOut(@ApiParam("退出用户的ticket") @CookieValue("ticket")String cookie){
        userService.loginOut(cookie);
        //重定向默认get   什么时候重定向？
        return "redirect:/login";
    }
}
