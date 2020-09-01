package com.ustc.software.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/1719:32
 */
//工具类
public class CommunityUtil {
    //生成随机字符串  ===用户上传图片 需要随机字符串起名字  激活码生成
    public static String getRandomString(){
        return UUID.randomUUID().toString().replaceAll("_", "");
    }
    //MD5加密 对密码加密
    //salt+password进行md5
    public static String md5(String password){
        //刚导入的commons-lang
        if(StringUtils.isBlank(password)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
     * 返回浏览器的信息由java对象转化为JSON字符串格式
     * @param code 状态码
     * @param msg   信息
     * @param map   业务数据
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String,Object>map){
        JSONObject json=new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map!=null){
            for (String key:map.keySet()){
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }
    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg, null);
    }
    public static String getJSONString(int code){
        return getJSONString(code, null, null);
    }
    /*
    public static void main(String[] args) {
        Map<String,Object> map=new HashMap<>();
        map.put("name", "张三");
        map.put("age", 23);
        System.out.println(CommunityUtil.getJSONString(0, "OK", map));

    }
    */
}
