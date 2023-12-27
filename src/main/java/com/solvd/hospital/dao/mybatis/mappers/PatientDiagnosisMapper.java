package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.PatientDiagnosis;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

public interface PatientDiagnosisMapper {
    @Insert("INSERT INTO patients_diagnoses (patient_id, diagnosis_id) " +
            "VALUES (#{patientId}, #{diagnosis.id})")
    void create(PatientDiagnosis patientDiagnosis);

    @Select("SELECT * FROM patients_diagnoses")
    @Results({
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "diagnosis", column = "diagnosis_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DiagnosisMapper.findById"))
    })
    List<PatientDiagnosis> findAll();

    @Select("SELECT * FROM patients_diagnoses WHERE patient_id = #{patientId}")
    @Results({
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "diagnosis", column = "diagnosis_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DiagnosisMapper.findById"))
    })
    List<PatientDiagnosis> findAllByPatientId(long patientId);

    @Select("SELECT * FROM patients_diagnoses WHERE patient_id = #{patientId} AND diagnosis_id = #{diagnosisId}")
    @Results({
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "diagnosis", column = "diagnosis_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DiagnosisMapper.findById"))
    })
    Optional<PatientDiagnosis> findByPatientIdAndDiagnosisId(@Param("patientId") long patientId, @Param("diagnosisId") long diagnosisId);


    @Update("UPDATE patients_diagnoses " +
            "SET patient_id = #{newPatientDiagnosis.patientId}, diagnosis_id = #{newPatientDiagnosis.diagnosis.id} " +
            "WHERE patient_id = #{patientDiagnosis.patientId} AND diagnosis_id = #{patientDiagnosis.diagnosis.id}")
    void update(@Param("patientDiagnosis") PatientDiagnosis patientDiagnosis, @Param("newPatientDiagnosis") PatientDiagnosis newPatientDiagnosis);

    @Delete("DELETE FROM patients_diagnoses " +
            "WHERE patient_id = #{patientId} AND diagnosis_id = #{diagnosis.id}")
    void delete(@Param("patientId") long patientId, @Param("diagnosisId") long diagnosisId);
}
