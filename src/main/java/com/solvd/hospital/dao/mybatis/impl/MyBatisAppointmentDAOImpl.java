package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.AppointmentDAO;
import com.solvd.hospital.dao.mybatis.mappers.AppointmentMapper;
import com.solvd.hospital.entities.Appointment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisAppointmentDAOImpl implements AppointmentDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisAppointmentDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Appointment create(Appointment appointment) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AppointmentMapper appointmentMapper = session.getMapper(AppointmentMapper.class);
            appointmentMapper.create(appointment);
            session.commit();
        }
        return appointment;
    }

    @Override
    public Optional<Appointment> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AppointmentMapper appointmentMapper = session.getMapper(AppointmentMapper.class);
            return Optional.ofNullable(appointmentMapper.findById(id));
        }
    }

    @Override
    public List<Appointment> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AppointmentMapper appointmentMapper = session.getMapper(AppointmentMapper.class);
            return appointmentMapper.findAll();
        }
    }

    @Override
    public List<Appointment> findByPatientId(long patientId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AppointmentMapper appointmentMapper = session.getMapper(AppointmentMapper.class);
            return appointmentMapper.findByPatientId(patientId);
        }
    }

    @Override
    public List<Appointment> findByDoctorId(long doctorId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AppointmentMapper appointmentMapper = session.getMapper(AppointmentMapper.class);
            return appointmentMapper.findByDoctorId(doctorId);
        }
    }

    @Override
    public Appointment update(Appointment appointment) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AppointmentMapper appointmentMapper = session.getMapper(AppointmentMapper.class);
            appointmentMapper.update(appointment);
            session.commit();
        }
        return appointment;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AppointmentMapper appointmentMapper = session.getMapper(AppointmentMapper.class);
            appointmentMapper.delete(id);
            session.commit();
        }
    }

    @Override
    public void deleteByPatient(long patientId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AppointmentMapper appointmentMapper = session.getMapper(AppointmentMapper.class);
            appointmentMapper.deleteByPatient(patientId);
            session.commit();
        }
    }
}
