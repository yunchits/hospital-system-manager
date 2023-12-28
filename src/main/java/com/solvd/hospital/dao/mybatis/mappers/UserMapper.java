package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.user.User;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role", javaType = Role.class)
    })
    Optional<User> getById(long id);

    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "role", column = "role", javaType = Role.class)
    })
    Optional<User> getByUsername(String username);

    @Insert("INSERT INTO users (username, password, role) " +
            "VALUES (#{username}, #{password}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(User user);

    @Update("UPDATE users SET username = #{user.username}, password = #{user.password}, role = #{user.role}" +
            "WHERE id = #{id}")
    void update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void delete(long id);
}
