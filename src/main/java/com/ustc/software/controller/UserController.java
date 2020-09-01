package com.ustc.software.controller;

import com.ustc.software.annotation.LoginRequired;
import com.ustc.software.entity.User;
import com.ustc.software.service.UserService;
import com.ustc.software.util.CommunityUtil;
import com.ustc.software.util.HostHolder;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/2622:40
 */
@Api
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger=LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String path;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    //存完要更高
    @Autowired
    private UserService userService;
    //更新的是当前登录用户的头像
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @ApiOperation("设置用户信息界面")
    //访问设置用户信息的页面
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @ApiOperation("上传图片")
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(@ApiParam("图片") MultipartFile headerImage, @ApiIgnore Model model){
        if (headerImage==null){
            model.addAttribute("error", "请上传图片");
            return "/site/setting";
        }
        //UUID 防止重名字   后缀不能变 通过正则
        /*String regex="/.[.]+";
        Pattern pattern = Pattern.compile(regex);
        String filename = headerImage.getOriginalFilename();
        Matcher matcher = pattern.matcher(filename);
        String suffix=matcher.group();*/
        String filename=headerImage.getOriginalFilename();
        //匹配.+之后的类型
        String suffix=filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error", "文件格式不正确");
            return "/site/setting";
        }
        //组成新的不重复的名字
        String newFilename=CommunityUtil.getRandomString()+suffix;
        //向服务器上传该文件
        try {
            headerImage.transferTo(new File(path+"/"+newFilename));
        } catch (IOException e) {
            logger.error("upload file error,error message:"+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常",e);
        }
        //更新当前用户的头像url
        User user = hostHolder.getUser();
        //Url格式http://localhost:8080/community/user/header/xxx.png[头像请求的url]
        String headUrl=domain+contextPath+"/user/header"+newFilename;
        userService.updateHeader(user.getId(), headUrl);
        return "redirect/index";
    }

    @RequestMapping(value = "/header/{filename}",method = RequestMethod.GET)
    //获取头像的请求方法==>通过response返回图片 所以返回void
    public void getHeader(@PathVariable("filename")String filename, HttpServletResponse response){
        String filePath=path+"/"+filename;
        //从本地读取图片传到response
        try (
                FileInputStream inputStream = new FileInputStream(filePath);
                OutputStream os = response.getOutputStream();
                ){
            String suffix=filename.substring(filename.lastIndexOf("."));
            response.setContentType("image/"+suffix);
            byte[]buffer=new byte[1024];
            int b=0;
            while ((b=inputStream.read(buffer))!=-1){
                os.write(buffer, 0, b) ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @RequestMapping("/updatepassword")
//    public String updatePassword(Model model,int userId,String oldPassword,String newPassword){
//        userService.findUserById(userId)
//    }

}
