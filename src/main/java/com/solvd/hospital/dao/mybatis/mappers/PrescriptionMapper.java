package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.Prescription;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PrescriptionMapper {
    @Insert("INSERT INTO prescriptions (doctor_id, patient_id, medication_id) VALUES (#{doctor.id}, #{patient.id}, #{medication.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Prescription prescription);

    @Select("SELECT * FROM prescriptions WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "doctor", column = "doctor_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorMapper.findById")),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "medication", column = "medication_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.MedicationMapper.findById"))
    })
    Prescription findById(long id);

    @Select("SELECT * FROM prescriptions")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "doctor", column = "doctor_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorMapper.findById")),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "medication", column = "medication_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.MedicationMapper.findById"))
    })
    List<Prescription> findAll();

    @Select("SELECT * FROM prescriptions WHERE patient_id = #{patientId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "doctor", column = "doctor_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorMapper.findById")),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "medication", column = "medication_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.MedicationMapper.findById"))
    })
    List<Prescription> findByPatientId(long patientId);

    @Update("UPDATE prescriptions SET doctor_id = #{doctor.id}, patient_id = #{patient.id}, medication_id = #{medication.id} WHERE id = #{id}")
    void update(Prescription prescription);

    @Delete("DELETE FROM prescriptions WHERE patient_id = #{patientId}")
    void deleteByPatientId(long patientId);

    @Delete("DELETE FROM prescriptions WHERE id = #{id}")
    void delete(long id);
}
