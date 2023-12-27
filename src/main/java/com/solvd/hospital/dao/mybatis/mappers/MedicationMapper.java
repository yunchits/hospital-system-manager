package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.Medication;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MedicationMapper {
    @Insert("INSERT INTO medications (medication_name, medication_description) " +
            "VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Medication medication);

    @Select("SELECT * FROM medications WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "medication_name"),
            @Result(property = "description", column = "medication_description")
    })
    Medication findById(long id);

    @Select("SELECT * FROM medications")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "medication_name"),
            @Result(property = "description", column = "medication_description")
    })
    List<Medication> findAll();

    @Update("UPDATE medications SET medication_name = #{name}, " +
            "medication_description = #{description} WHERE id = #{id}")
    void update(Medication medication);

    @Delete("DELETE FROM medications WHERE id = #{id}")
    void delete(long id);
}
