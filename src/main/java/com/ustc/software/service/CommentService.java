package com.ustc.software.service;

import com.ustc.software.dao.CommentMapper;
import com.ustc.software.entity.Comment;
import com.ustc.software.util.CommunityConstant;
import com.ustc.software.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpUtils;
import java.util.List;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/3012:58
 */
@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    public List<Comment> findCommentsByEntity(int entityType,int entityId,int offset,int limit){
        return commentMapper.selectCommentByEntity(entityType,entityId,offset,limit);
    }
    public int getCommentsCountByEntity(int entityType,int entityId){
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    /**
     * 数据库里增加评论，修改帖子的评论的数量====》多次数据库操作 所以需要为该方法添加事务
     * @param comment
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        if (comment==null){
            throw  new IllegalArgumentException("参数不能为空");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        //库里添加评论
        int rows=commentMapper.insertComment(comment);
        //更新评论数量==>当评论为帖子的评论是才更新
        if (comment.getEntityType()==ENTITY_TYPE_POST){
            //针对的帖子的Id在controller层传过来
            int count = commentMapper.selectCountByEntity(ENTITY_TYPE_POST, comment.getEntityId());
            //去库里拿的 是新的
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return rows;
    }
}
