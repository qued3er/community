package com.ustc.software.service;

import com.ustc.software.dao.DiscussPostMapper;
import com.ustc.software.entity.DiscussPost;
import com.ustc.software.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1616:27
 */
/*在查询帖子时  页面不显示userId  而是显示对应的用户名
 解决办法：1.写sql时关联user表 同时查出
 2、在业务层/controller层做关联 通过User的getUserById 找对应discussPost的User
 采用2 为了redis缓存*/
@Service
public class DiscussPostService {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId,int limit,int offset){
        return discussPostMapper.selectDiscussPost(userId, limit, offset);
    }
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 新增帖子
     * @param discussPost 帖子本体
     * @return 插入结果
     */
    public int addDiscussPost(DiscussPost discussPost){
        if (discussPost==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //如果网页中出现的有script的标签  则需要被转移  否则会被浏览器翻译成script标签
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //对post做敏感词过滤==>title与content 过滤
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        return discussPostMapper.insertDiscussPost(discussPost);
    }

    /**
     *
     * @param id 帖子id
     * @return 帖子
     */
    public DiscussPost findDiscussPost(int id){
        return discussPostMapper.getDiscussPostById(id);
    }

    public int updateCommentCount(int id,int commentCount){
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
