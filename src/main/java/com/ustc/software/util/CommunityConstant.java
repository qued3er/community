package com.ustc.software.util;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1814:49
 */
public interface CommunityConstant {
    /**
     * 激活成功
     * */
    static final int ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     * */
    static final int ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     * */
    static final int ACTIVATION_FAIL = 2;
    /**
     * 默认状态的登录凭证的超时时间
     * */
    static final int DEFAULT_EXPIRED_SECONDS=3600*12;
    /**
     * rememberme的登录凭证超时时间
     * */
    static final int REMEMBER_EXPIRED_SECONDS=3600*24*100;
    /**
     * 实体类型：帖子
     */
    static final int ENTITY_TYPE_POST = 1;
    /**
     * 实体类型：评论
     */
    static final int ENTITY_TYPE_COMMENT = 2;
}
