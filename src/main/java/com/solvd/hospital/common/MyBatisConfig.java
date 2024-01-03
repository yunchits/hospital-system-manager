package com.solvd.hospital.common;

import lombok.Getter;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisConfig {

    @Getter
    private static final SqlSessionFactory sqlSessionFactory;

    private MyBatisConfig() {
    }

    static {
        try (InputStream is = Resources.getResourceAsStream("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(is);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing SqlSessionFactory: " + e);
        }
    }
}
