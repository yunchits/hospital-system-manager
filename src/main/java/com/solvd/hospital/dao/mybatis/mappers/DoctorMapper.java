package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.doctor.Doctor;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DoctorMapper {
    @Insert("INSERT INTO doctors (first_name, last_name, specialization, salary_id) VALUES " +
            "(#{firstName}, #{lastName}, #{specialization}, #{salary.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Doctor doctor);

    @Select("SELECT * FROM doctors WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "specialization", column = "specialization"),
            @Result(property = "salary", column = "salary_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorSalaryMapper.findById"))
    })
    Doctor findById(long id);

    @Select("SELECT * FROM doctors")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "specialization", column = "specialization"),
            @Result(property = "salary", column = "salary_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorSalaryMapper.findById"))
    })
    List<Doctor> findAll();

    @Update("UPDATE doctors SET first_name = #{firstName}, last_name = #{lastName}, " +
            "specialization = #{specialization}, salary_id = #{salary.id} WHERE id = #{id}")
    void update(Doctor doctor);

    @Delete("DELETE FROM doctors WHERE id = #{id}")
    void delete(long id);
}
