package com.ustc.software.service;

import com.ustc.software.dao.DiscussPostMapper;
import com.ustc.software.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId,int limit,int offset){
        return discussPostMapper.selectDiscussPost(userId, limit, offset);
    }
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
