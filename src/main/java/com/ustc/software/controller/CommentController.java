package com.ustc.software.controller;

import com.ustc.software.entity.Comment;
import com.ustc.software.service.CommentService;
import com.ustc.software.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/3016:45
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;

    /**
     *  做增、删、改的时候最好用重定向，因为如果不用重定向，每次刷新页面就相当于再请求一次，
     *  就可能会做额外的操作，导致数据不对。
     * @param discussPostId 新增评论的帖子的id
     * @param comment comment前端只传过来 entity_type,entity_id,content,target_id 别的需要补充
     * @return 添加成功返回当前帖子页面
     */
    @RequestMapping(path = "/add/{discusspostid}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discusspostid")String discussPostId, Comment comment){
        comment.setCreateTime(new Date());
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        commentService.addComment(comment);
        return "redirect:/discuss/detail/"+discussPostId;
    }
}
