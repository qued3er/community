package com.ustc.software.dao;

import com.ustc.software.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1615:54
 */
@Mapper
//    @Repository
public interface DiscussPostMapper {
    //分页查询 ==>起始行offset 最多显示多少：limit ==》一共多少页？需要知道一共多少条数据 下个方法
    // userId 是为了个人主页 查自己发的帖子 首页查的时候
    //不会传入userId  所以默认是0   所以当是0的时候我们就屏蔽userId
    //动态sql
    List<DiscussPost> selectDiscussPost(int userId,int limit,int offset);

    //查询一共有多少数据  加userId是为了扩展个人主页  动态sql 为0忽视


    //@Param  给参数取别名  方法只有一个参数时 并且在动态sql<if>使用 必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);
}
