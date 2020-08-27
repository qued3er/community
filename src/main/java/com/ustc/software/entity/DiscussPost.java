package com.ustc.software.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1615:47
 */
@ApiModel("帖子")
//帖子实体类
public class DiscussPost {
    private int id;
    @ApiModelProperty("帖子主人")
    private int userId;
    @ApiModelProperty("帖子标题")
    //帖子标题
    private String title;
    @ApiModelProperty("帖子内容")
    //内容
    private String content;
    @ApiModelProperty("帖子类型 0普通 1置顶")
    //类型 0普通 1置顶
    private int type;
    @ApiModelProperty("帖子状态 0正常 1精华 2拉黑")
    //状态 0正常 1精华 2拉黑
    private int status;
    private Date createTime;
    @ApiModelProperty("评论数")
    //评论的数量  冗余字段 为了减少联合查询
    private int commentCount;
    @ApiModelProperty("帖子序列 排第几")
    //按照score给帖子排名
    private double score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTime=" + createTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }
}
