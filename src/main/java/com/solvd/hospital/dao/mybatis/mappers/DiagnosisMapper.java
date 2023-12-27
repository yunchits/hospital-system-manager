package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.Diagnosis;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

public interface DiagnosisMapper {
    @Insert("INSERT INTO diagnoses (diagnosis_name, diagnosis_description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Diagnosis diagnosis);

    @Select("SELECT * FROM diagnoses WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "diagnosis_name"),
            @Result(property = "description", column = "diagnosis_description")
    })
    Diagnosis findById(long id);

    @Select("SELECT * FROM diagnoses")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "diagnosis_name"),
            @Result(property = "description", column = "diagnosis_description")
    })
    List<Diagnosis> findAll();

    @Select("SELECT * FROM diagnoses WHERE diagnosis_name = #{name}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "diagnosis_name"),
            @Result(property = "description", column = "diagnosis_description")
    })
    Optional<Diagnosis> findByDiagnosisName(String name);

    @Update("UPDATE diagnoses SET diagnosis_name = #{name}, diagnosis_description = #{description} WHERE id = #{id}")
    void update(Diagnosis diagnosis);

    @Delete("DELETE FROM diagnoses WHERE id = #{id}")
    void delete(long id);
}
