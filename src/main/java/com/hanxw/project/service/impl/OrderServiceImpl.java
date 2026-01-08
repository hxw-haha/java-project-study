package com.hanxw.project.service.impl;

import com.hanxw.project.entity.UserEntity;
import com.hanxw.project.mapper.OrderMapper;
import com.hanxw.project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public UserEntity selectUserWithOrders(Long userId) {
        // MyBatis 执行流程解释：
        // 1. 通过 SqlSessionFactory 获取 SqlSession
        // 2. SqlSession 获取 Mapper 代理对象（动态代理实现接口方法）
        // 3. 调用 selectUserWithOrders → MappedStatement → Executor 执行 SQL
        // 4. ResultSet → resultMap 映射 → Java 对象（包括 collection 嵌套）
        // 5. 一级缓存：同一 SqlSession 内相同 SQL 只执行一次
        // 6. 二级缓存：不同 SqlSession 可共享（需 @CacheNamespace）
        return orderMapper.selectUserWithOrders(userId);
    }

}
