package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.patient.Patient;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.EnumTypeHandler;

import java.util.List;

public interface PatientMapper {
    @Insert("INSERT INTO patients (first_name, last_name, birth_date, gender) " +
            "VALUES (#{firstName}, #{lastName}, #{birthDate}, #{gender})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Patient patient);

    @Select("SELECT * FROM patients WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "birthDate", column = "birth_date"),
            @Result(property = "gender", column = "gender", typeHandler = EnumTypeHandler.class)
    })
    Patient findById(long id);

    @Select("SELECT * FROM patients")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "birthDate", column = "birth_date"),
            @Result(property = "gender", column = "gender", typeHandler = EnumTypeHandler.class)
    })
    List<Patient> findAll();

    @Update("UPDATE patients SET first_name = #{firstName}, last_name = #{lastName}, " +
            "birth_date = #{birthDate}, gender = #{gender} WHERE id = #{id}")
    void update(Patient patient);

    @Delete("DELETE FROM patients WHERE id = #{id}")
    void delete(long id);
}
