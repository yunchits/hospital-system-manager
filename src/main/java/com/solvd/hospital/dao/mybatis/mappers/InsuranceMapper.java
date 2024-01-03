package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.patient.Insurance;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

public interface InsuranceMapper {
    @Insert("INSERT INTO patient_insurances (patient_id, policy_number, expiration_date, coverage_amount, type, insurance_provider) " +
            "VALUES (#{patientId}, #{policyNumber}, #{expirationDate}, #{coverageAmount}, #{type}, #{insuranceProvider})")
    void create(Insurance insurance);

    @Select("SELECT * FROM patient_insurances")
    @Results({
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "policyNumber", column = "policy_number"),
            @Result(property = "expirationDate", column = "expiration_date"),
            @Result(property = "coverageAmount", column = "coverage_amount"),
            @Result(property = "type", column = "type"),
            @Result(property = "insuranceProvider", column = "insurance_provider")
    })
    List<Insurance> findAll();

    @Select("SELECT * FROM patient_insurances WHERE patient_id = #{id}")
    @Results({
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "policyNumber", column = "policy_number"),
            @Result(property = "expirationDate", column = "expiration_date"),
            @Result(property = "coverageAmount", column = "coverage_amount"),
            @Result(property = "type", column = "type"),
            @Result(property = "insuranceProvider", column = "insurance_provider")
    })
    Optional<Insurance> findById(long id);


    @Update("UPDATE patient_insurances SET policy_number = #{policyNumber}, expiration_date = #{expirationDate}, " +
            "coverage_amount = #{coverageAmount}, type = #{type}, insurance_provider = #{insuranceProvider} " +
            "WHERE patient_id = #{patientId}")
    void update(Insurance insurance);

    @Delete("DELETE FROM patient_insurances WHERE patient_id = #{patientId}")
    void deleteByPatientId(long patientId);
}
