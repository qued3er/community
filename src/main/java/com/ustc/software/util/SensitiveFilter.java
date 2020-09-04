package com.ustc.software.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @Title:
 * @Description:前缀树过滤敏感词
 * @date 2020/8/2814:27
 */
class Node{
    //李三 李四  王五 王六
    //默认是false 新添加改true
    private boolean isEnd=false;
    //key是当前的节点的某个字符  value指向该字符node的。
    private Map<Character,Node>dictTree=new HashMap<>();

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
    //添加子节点
    public void addNode(Character c,Node node){
        dictTree.put(c, node);
    }
    //获取子节点
    public Node getNode(Character c){
        return dictTree.get(c);
    }
}

//交给容器 各个层次都可用
@Component
public class SensitiveFilter {
    private static final Logger logger=LoggerFactory.getLogger(SensitiveFilter.class);

    //拿去替换敏感词
    private static final String REPLACE  = "***";
    //前缀树根节点
    private Node root=new Node();
    //spring启动 初始化该component  后就会执行该init  postconstruct什么时候执行？用途？
    //@PostConstruct和@PreDestroy。这两个注解被用来修饰一个非静态的void()方法 。
    //Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
    @PostConstruct
    public void init(){
        try(
        //从classes下读取配置文件================》读取类路径下的文件
        //字节流转成字符流  带缓冲的
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            String keyWord;
            while ((keyWord=reader.readLine())!=null){
                //添加到前缀树中去
                this.addKeyWord(keyWord);
            }
        }catch (IOException e) {
            logger.error("加载敏感词失败"+e.getMessage());
        }
    }

    //添加词到前缀树
    private void addKeyWord(String keyWord) {
        Node tempNode=root;
        char[] target = keyWord.toCharArray();
        for (int i = 0; i < target.length; i++) {
            Node subNode = tempNode.getNode(target[i]);
            if (subNode == null) {
                subNode=new Node();
                tempNode.addNode(target[i],subNode);
            }
            tempNode=subNode;
            //设置结束表示
            if (i==target.length-1){
                tempNode.setEnd(true);
            }
        }
    }

    /**
     * 把敏感词替换掉  返回替换掉的字符串
     * @param text 待过滤文本
     * @return 已过滤文本
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }
        Node temp=root;
        //储存结果 要不断追加
        StringBuilder stringBuilder=new StringBuilder();
        char[] target = text.toCharArray();
        //逐个的查以某个字符开始 是否在该树中。
        // 跳过符号  黄☆☆☆色  可绕过
        for (int i=0;i<target.length;i++){
            //检查以该字符起始的某个串是否在前缀树中
            for (int j=i;j< target.length;j++){
                if (isSymbol(target[j])){
                    continue;
                }
                //isEnd
                if (temp.getNode(target[j])!=null){
                    //替换从i->j 并接着从j+1开始
                    if (temp.getNode(target[j]).isEnd()){
                        stringBuilder.append(REPLACE);
                        i=j;
                        break;
                    }
                    temp=temp.getNode(target[i]);
                }else {
                    stringBuilder.append(target, i, j+1);
                    i=j;
                    break;
                }
            }
            temp=root;
        }
        return stringBuilder.toString();
    }

    /**
     * true:当该字符不是ascii数字的时候并且并且c的位于 小于2E80  大于9FFF
     *
     * @param c
     * @return
     */
    public boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c)&& (c < 0x2E80 || c > 0x9FFF);
    }

}
