package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.Appointment;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

public interface AppointmentMapper {
    @Insert("INSERT INTO appointments (patient_id, doctor_id, appointment_datetime) " +
            "VALUES (#{patient.id}, #{doctor.id}, #{appointmentDateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Appointment appointment);

    @Select("SELECT * FROM appointments WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "doctor", column = "doctor_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorMapper.findById")),
            @Result(property = "appointmentDateTime", column = "appointment_datetime")
    })
    Optional<Appointment> findById(long id);

    @Select("SELECT * FROM appointments")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "doctor", column = "doctor_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorMapper.findById")),
            @Result(property = "appointmentDateTime", column = "appointment_datetime")
    })
    List<Appointment> findAll();

    @Select("SELECT * FROM appointments WHERE patient_id = #{patientId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "doctor", column = "doctor_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorMapper.findById")),
            @Result(property = "appointmentDateTime", column = "appointment_datetime")
    })
    List<Appointment> findByPatientId(long patientId);

    @Select("SELECT * FROM appointments WHERE doctor_id = #{doctorId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patient_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.PatientMapper.findById")),
            @Result(property = "doctor", column = "doctor_id", one = @One(select = "com.solvd.hospital.dao.mybatis.mappers.DoctorMapper.findById")),
            @Result(property = "appointmentDateTime", column = "appointment_datetime")
    })
    List<Appointment> findByDoctorId(long doctorId);

    @Update("UPDATE appointments SET patient_id = #{patient.id}, doctor_id = #{doctor.id}, " +
            "appointment_datetime = #{appointmentDateTime} WHERE id = #{id}")
    void update(Appointment appointment);

    @Delete("DELETE FROM appointments WHERE id = #{id}")
    void delete(long id);

    @Delete("DELETE FROM appointments WHERE patient_id = #{patientId}")
    void deleteByPatient(long patientId);
}
