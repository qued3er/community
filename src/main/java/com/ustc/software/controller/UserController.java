package com.ustc.software.controller;

import com.ustc.software.service.UserService;
import com.ustc.software.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

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
        headerImage.getOriginalFilename();
    }
}
