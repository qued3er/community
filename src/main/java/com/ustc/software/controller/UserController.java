package com.ustc.software.controller;

import com.ustc.software.service.UserService;
import com.ustc.software.util.CommunityUtil;
import com.ustc.software.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/2622:40
 */
@Controller
@RequestMapping("/user")
public class UserController {

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
    //访问设置用户信息的页面
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
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
        //组成新的不重复的名字
        String newFilename=CommunityUtil.getRandomString()+suffix;
        //向服务器上传该文件
//        File file=new File()
        return "site/index";
    }
}
