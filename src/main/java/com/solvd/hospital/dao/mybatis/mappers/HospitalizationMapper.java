package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.Hospitalization;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

public interface HospitalizationMapper {
    @Insert("INSERT INTO hospitalizations (patient_id, admission_date, discharge_date) VALUES (#{patient.id}, #{admissionDate}, #{dischargeDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Hospitalization hospitalization);

    @Select("SELECT * FROM hospitalizations")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "admissionDate", column = "admission_date"),
            @Result(property = "dischargeDate", column = "discharge_date")
    })
    List<Hospitalization> findAll();

    @Select("SELECT * FROM hospitalizations WHERE patient_id = #{patientId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "admissionDate", column = "admission_date"),
            @Result(property = "dischargeDate", column = "discharge_date")
    })
    List<Hospitalization> findByPatientId(long patientId);

    @Select("SELECT * FROM hospitalizations WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "admissionDate", column = "admission_date"),
            @Result(property = "dischargeDate", column = "discharge_date")
    })
    Optional<Hospitalization> findById(long id);

    @Update("UPDATE hospitalizations SET patient_id = #{patient.id}, admission_date = #{admissionDate}, discharge_date = #{dischargeDate} WHERE id = #{id}")
    void update(Hospitalization hospitalization);

    @Delete("DELETE FROM hospitalizations WHERE id = #{id}")
    void delete(long id);

}
