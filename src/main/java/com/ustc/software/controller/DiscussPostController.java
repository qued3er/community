package com.ustc.software.controller;

import com.ustc.software.entity.Comment;
import com.ustc.software.entity.DiscussPost;
import com.ustc.software.entity.Page;
import com.ustc.software.entity.User;
import com.ustc.software.service.CommentService;
import com.ustc.software.service.DiscussPostService;
import com.ustc.software.service.UserService;
import com.ustc.software.util.CommunityConstant;
import com.ustc.software.util.CommunityUtil;
import com.ustc.software.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/2914:10
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    /**
     * 添加帖子 异步请求
     * @param title 标题
     * @param content  内容
     * @return  插入的结果
     */
    @RequestMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if (user==null){
            return CommunityUtil.getJSONString(403, "您还未登录");
        }
        DiscussPost discussPost=new DiscussPost();
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setUserId(user.getId());
        //type与status默认是0不用初始化
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);
        return CommunityUtil.getJSONString(0, "ok");
    }

    /**
     * 查询帖子并实现评论分页
     * @param discussPostId
     * @param model
     * @param page mvc会自动把参数放到model中
     * @return
     */
    @RequestMapping(path = "/detail/{discusspostid}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discusspostid") int discussPostId, Model model, Page page){
        DiscussPost post = discussPostService.findDiscussPost(discussPostId);
        model.addAttribute("post", post);
        //查post对应的user
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        //设置分页的信息
        page.setLimit(5);
        page.setPath("/discuss/detail"+discussPostId);
        //利用post总的冗余commentcount字段减少数据库查询次数
        page.setRows(post.getCommentCount());
        //评论：给帖子的评论   回复：评论的评论
        List<Comment> comments = commentService.findCommentsByEntity(ENTITY_TYPE_POST, discussPostId,
                page.getOffset(), page.getLimit());
        //评论VO列表
        List<Map<String,Object>>commentVOList=new ArrayList<>();
        for (Comment comment:
            comments) {
            //评论VO
            Map<String,Object>commentVO=new HashMap<>();
            commentVO.put("comment", comment);
            commentVO.put("user", userService.findUserById(comment.getUserId()));
            //回复的列表 ==>不分页了
            List<Comment> replyComments = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getEntityId()
                    , 0, Integer.MAX_VALUE);
            List<Map<String,Object>> replyVOList=new ArrayList<>();
            if (replyComments!=null){
                for (Comment replyComment :
                        replyComments) {
                    Map<String, Object> replyVO = new HashMap<>();
                    replyVO.put("reply", replyComment);
                    replyVO.put("user", userService.findUserById(replyComment.getUserId()));
                    //直接回复没目标id  回复评论的评论才有目标
                    User targetUser=replyComment.getTargetId()==0?
                            null:userService.findUserById(replyComment.getUserId());
                    replyVO.put("target", targetUser);
                    replyVOList.add(replyVO);
                }
            }
            commentVO.put("replys", replyVOList);
            int replyCount = commentService.getCommentsCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
            commentVO.put("replyCount", replyCount);
            commentVOList.add(commentVO);
        }
        model.addAttribute("comments", commentVOList);
        return "/site/discuss-detail.html";
    }
}
