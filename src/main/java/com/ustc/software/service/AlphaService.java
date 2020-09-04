package com.ustc.software.service;

import com.ustc.software.dao.DiscussPostMapper;
import com.ustc.software.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/2916:43
 */
@Service
public class AlphaService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    //demo  把新增用户与新增帖子当做一个事务 做一个demo
    @Autowired
    private TransactionTemplate transactionTemplate;
    /*
    * 为方法添加声明式事务， 如果不指定 事务的隔离级别，就是默认的 可配置isolation配置事务的隔离级别，此外还需配置该事务的
    * 传播属性，常用的三个传播属性：
    * REQUIRED：支持当前事务（外部事务），不存在则创建新事务
    * REQUIRES_NEW：创建新事务，暂停当前事务
    * NESTED：如果存在当前事务，则嵌套在该事务中执行(有独立的提交和回滚)，否则就等同于required
    * */
    /*另一种方法 编程式事务。==》利用TransactionTemplate
    优点：可以管理方法内的某个片段
    * */
//    @Transactional(isolation = Isolation.READ_COMMITTED ,propagation = Propagation.REQUIRED)
//    public Object save(){
//
//    }
//    public Object save2(){
//        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
//        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        return transactionTemplate.execute(new TransactionCallback<Object>() {
//            /*回调方法*/
//            @Override
//            public Object doInTransaction(TransactionStatus status) {
//
//            }
//        });
//    }
}
