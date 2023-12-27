package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.doctor.DoctorSalary;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DoctorSalaryMapper {
    @Insert("INSERT INTO doctor_salaries (salary, payment_date) VALUES (#{salary}, #{paymentDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(DoctorSalary doctorSalary);

    @Select("SELECT * FROM doctor_salaries WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "salary", column = "salary"),
            @Result(property = "paymentDate", column = "payment_date")
    })
    DoctorSalary findById(long id);

    @Select("SELECT * FROM doctor_salaries")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "salary", column = "salary"),
            @Result(property = "paymentDate", column = "payment_date")
    })
    List<DoctorSalary> findAll();

    @Update("UPDATE doctor_salaries SET salary = #{salary}, payment_date = #{paymentDate} WHERE id = #{id}")
    void update(DoctorSalary doctorSalary);

    @Delete("DELETE FROM doctor_salaries WHERE id = #{id}")
    void delete(long id);
}
