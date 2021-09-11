package com.example.test;

import com.example.dao.UserDao;
import com.example.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * @author: ming
 * @date: 2021/9/11 14:32
 */
public class UserDaoTest {

    private SqlSession session;
    private InputStream resourceAsStream;
    private UserDao userDao;

    @Before
    public void init() throws IOException {
        resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = builder.build(resourceAsStream);

        session = sqlSessionFactory.openSession();
        userDao = session.getMapper(UserDao.class);
    }

    @After
    public void destroy() throws IOException {
        session.commit();
        session.close();
        resourceAsStream.close();
    }

    @Test
    public void queryByUsernameAndPassword() {
        User user = userDao.queryByUsernameAndPassword("aaa","aaa");
        System.out.println(user);
    }

    @Test
    public void saveUser() {
        int i = userDao.saveUser("ccc","ccc");
        System.out.println(i);
    }
}