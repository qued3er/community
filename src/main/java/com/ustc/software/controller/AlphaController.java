package com.ustc.software.controller;

import com.ustc.software.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1213:16
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @RequestMapping("/hello")
    @ResponseBody
    //将json格式的数据转为java对象
    public String sayhello(){
        return "hello";
    }
    //底层方式,由于可以通过response对象  向浏览器输出数据===》返回值为空
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        //获取请求数据  请求方法  get？post？   请求路径   ===》对应第一行
        System.out.println(request.getMethod());
        System.out.println(request.getContextPath());
        //获取请求头所有的key【返回请求行的所有key 是一个迭代器】
        Enumeration<String> headerNames = request.getHeaderNames();
        //迭代获取所有的keyvalue
        while (headerNames.hasMoreElements()){
            String s = headerNames.nextElement();
            String header = request.getHeader(s);
            System.out.println(s+":"+header);
        }
        //获取key为code的请求体的参数,    ?之后的参数==》对应于具体的请求呢？
        System.out.println(request.getParameter("code"));
        //返回响应数据
        //指定返回什么数据
        response.setContentType("text/html;charset=utf-8");

//        finally无法关闭try内创建的write
        //获取输出流返回数据
        PrintWriter writer =null;
        try {
            writer=response.getWriter();
            //写入所有的html行
            writer.write("<h1>牛客网<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
    }
    //get请求  [传参：1.?拼接 2.直接写进路径去获取]
    // 分页  当前第几页   一共多少条路径
    //请求数据的绑定   dispatcher 检测到与controller参数名字相同的request当中的数据直接复制给参数
    //使用@RequestParam对参数进行更详尽的设定   名字不一样的绑定  是否为必须   默认值
    @RequestMapping(path = "/name",method = RequestMethod.GET)
    //带response返回json格式的字符串，不带的话返回静态html页面
    @ResponseBody
    public String getName(String name,int value){
        System.out.println("get name:\t"+name+value);
        return "success";
    }
    //第二种get参数作为请求路径的一部分 {id}


    @RequestMapping(path = "/addUser",method = RequestMethod.POST)
    //post请求 [浏览器向服务器提交数据==》通过表单]

    //向浏览器响应数据   响应html页面 数据不加@ResponseBody不加@ResponseBody不加@ResponseBody不加@ResponseBody
    //不加@ResponseBody   默认返回html    返回一般是MOdelandView【view是thymleaf的动态模板】  ===》
    // 返回给dispatcherservlet   然后它再给模板引擎渲染   模板引擎拿着 model去渲染thymleaf的动态html模板

    public ModelAndView addUser(String name,int age){
        System.out.println("enter");
        ModelAndView mv =new ModelAndView();
        mv.setViewName("user");
        mv.addObject("name", name);
        mv.addObject("age", age);
        return mv;
    }
    //返回html 2  直接retrun  view的路径     参数需要有model   model什么时候实例化的？？？     [简洁 ]
    //方式2 直接return 返回的视图，参数加model用来接收数据
    @RequestMapping("/addLanguage")
    public String addLanguage(Model model,String language){
        model.addAttribute("language", language);
        return "user";
    }
    //响应JSON数据(一般是在异步请求中：当前网页不动，但是悄悄的访问了服务器   该昵称已经被使用)  异步请求？？？
    //JAVA对象[集合  普通对象等] 无法转化为其他的对象   java对象->json->js对象    任何语言都有字符串类型  JSON起衔接   跨语言
    //加不加@responseBody===》加了要返回JSON格式的数据[对return的数据按照json格式来解析]，不加默认返回html页面。
    // 【开发者工具看响应类型 content type】==》java转json
    //responsebody如何把map依据list转为json



    //cookie 创建  设置有效范围   [默认在内存 关掉就小时]cookie的生成时间[设了生存时间就保存到硬盘里]  放到response里
    //cookie不安全
    @RequestMapping("/set")
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        Cookie cookie=new Cookie("code", CommunityUtil.getRandomString());
        cookie.setPath("/community/alpha");
        cookie.setMaxAge(60);
        response.addCookie(cookie);
        return "set Success";
    }
    @RequestMapping("/get")
    @ResponseBody
    public String getCookie(@CookieValue("code")String cookie, HttpServletRequest request){
        System.out.println(cookie);
        return "get cookie";
    }

}
