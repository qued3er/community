package com.ustc.software.dao;

import com.ustc.software.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/3012:27
 */
@Repository
@Mapper
public interface CommentMapper {
    /**
     * 根据entity查询 offset->offset+limit的评论
     * @param entityType entity的类型
     * @param entityId entity的id
     * @param offset 分页的offset
     * @param limit 分页的limit
     * @return 该entity的所有commnet
     */
    List<Comment> selectCommentByEntity(int entityType,int entityId,int offset,int limit);

    /**
     * 查询某个entity的评论总数用以判断页数
     * @param entityType 评论针对的实体类类型
     * @param entityId  评论针对的实体类类型的id
     * @return
     */
    int selectCountByEntity(int entityType,int entityId);

    /**
     * 插入评论
     * @param comment
     * @return
     */
    int insertComment(Comment comment);
}
