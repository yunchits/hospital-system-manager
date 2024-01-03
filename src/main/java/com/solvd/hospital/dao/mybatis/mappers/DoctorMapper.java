package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.doctor.Doctor;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

public interface DoctorMapper {
    @Insert("INSERT INTO doctors (first_name, last_name, specialization, user_id) " +
            "VALUES (#{firstName}, #{lastName}, #{specialization}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createWithUserId(Doctor doctor);

    @Insert("INSERT INTO doctors (first_name, last_name, specialization) " +
            "VALUES (#{firstName}, #{lastName}, #{specialization})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Doctor doctor);

    @Select("SELECT * FROM doctors WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "specialization", column = "specialization")
    })
    Optional<Doctor> findById(long id);

    @Select("SELECT * FROM doctors WHERE user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "specialization", column = "specialization")
    })
    Optional<Doctor> findByUserId(long userId);

    @Select("SELECT * FROM doctors")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "specialization", column = "specialization"),
    })
    List<Doctor> findAll();

    @Update("UPDATE doctors SET first_name = #{firstName}, last_name = #{lastName}, " +
            "specialization = #{specialization} WHERE id = #{id}")
    void update(Doctor doctor);

    @Update("UPDATE doctors SET user_id = #{userId} WHERE id = #{id}")
    void updateUserId(Doctor doctor);

    @Delete("DELETE FROM doctors WHERE id = #{id}")
    void delete(long id);
}
