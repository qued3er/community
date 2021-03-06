package com.ustc.software.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1618:28
 */
/*
封装分页相关的信息
* */
@ApiModel("帖子分页")
public class Page {
    @ApiModelProperty("当前页码")
    //当前页码  默认值
    private int current =1;
    @ApiModelProperty("显示上限个数")
    //显示的上限
    private int limit=10;
    @ApiModelProperty("总条数")
    //数据总数  用于计算总页数  总数/上限
    private int rows;
    @ApiModelProperty("分页的页数连接")
    //查询路径  分页的页数链接
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current>=1){
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit>=1&&limit<=100) {
            this.limit = limit;
        }
    }

    public int getRows() {

        return rows;
    }

    public void setRows(int rows) {
        if (rows>=0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //dao要的是offset
    public int getOffset(){
        return current*limit-limit;
    }
    //分页  需要获取总页数  防止超过总页数  总页数的边界
    public int getTotal(){
        if ((rows%limit)==0){
            return rows/limit;
        }else {
            return (rows/limit)+1;
        }
    }

    //假设一共有100页   下标不能100全显示只能显示一个范围
    public int getFrom(){
        int from=current-2;
        //小心下标溢出
        if (from<1){
            return 1;
        }else {
            return from;
        }
    }
    public int getTo(){
        int end=current+2;
        int total=getTotal();
        if (end>total){
            return total;
        }else {
            return end;
        }
    }
    @Override
    public String toString() {
        return "Page{" +
                "current=" + current +
                ", limit=" + limit +
                ", rows=" + rows +
                ", path='" + path + '\'' +
                '}';
    }
}
