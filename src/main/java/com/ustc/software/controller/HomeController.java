package com.ustc.software.controller;

import com.ustc.software.entity.DiscussPost;
import com.ustc.software.entity.Page;
import com.ustc.software.entity.User;
import com.ustc.software.service.DiscussPostService;
import com.ustc.software.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1617:04
 */
@Api("index页面访问操作")
@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @ApiOperation("分页请求index页面")
    //主页请求
    @RequestMapping("/index")
    public String getIndexPage(Model model, @ApiParam("分页的参数") Page page){
        //方法调用之前：springmvc会直接把model与page实例化了，然后把page放入model 所以可以直接在thymleaf中使用page==》
        // 不用model.addAttribute()
        //分页的实现
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
//        model.addAttribute("page", page);
        //分页查询 ==并把当中的userId对应的User查到
        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(0, page.getLimit(), page.getOffset());
        //记录的表现
        //如何对User 以及其对应的Post进行组合？
        //方法1 新创建一个实体类，属性包括username 以及post对应的属性
        //方法2 通过Map  一个Map 只装一个post及其对应的User
        List<Map<String,Object>> discussPostWithUser=new ArrayList<>();
        for (DiscussPost discussPost :
                discussPosts) {
            User user = userService.findUserById(discussPost.getUserId());
            Map<String,Object>map =new HashMap<>();
            map.put("post", discussPost);
            map.put("user", user);
            discussPostWithUser.add(map);
        }
        //dubug用到的测试例子
//        for (Map<String, Object> map :
//                discussPostWithUser) {
//            System.out.println(map);
//        }
        //thymleaf的渲染出现了问题
        //向model中添加  给模板引擎去渲染
        model.addAttribute("discussPosts", discussPostWithUser);
//        model.addAttribute()
        //返回templete下的index.html
        return "/index";
    }
}
