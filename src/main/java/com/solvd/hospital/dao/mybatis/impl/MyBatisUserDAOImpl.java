package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.UserDAO;
import com.solvd.hospital.dao.mybatis.mappers.UserMapper;
import com.solvd.hospital.entities.user.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Optional;

public class MyBatisUserDAOImpl implements UserDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisUserDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public User create(User user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            userMapper.create(user);
            session.commit();
        }
        return user;
    }

    @Override
    public Optional<User> getById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            return userMapper.getById(id);
        }
    }

    @Override
    public Optional<User> getByUsername(String username) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            return userMapper.getByUsername(username);
        }
    }

    @Override
    public User update(User user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            userMapper.update(user);
            session.commit();
        }
        return user;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            userMapper.delete(id);
            session.commit();
        }
    }

    @Override
    public boolean isUsernameUnique(String username) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            return userMapper.isUsernameUnique(username);
        }
    }
}
