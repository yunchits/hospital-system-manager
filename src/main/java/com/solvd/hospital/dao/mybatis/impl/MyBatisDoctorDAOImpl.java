package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.DoctorDAO;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.dao.mybatis.mappers.DoctorMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisDoctorDAOImpl implements DoctorDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisDoctorDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Doctor create(Doctor doctor) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorMapper doctorMapper = session.getMapper(DoctorMapper.class);
            doctorMapper.create(doctor);
            session.commit();
        }
        return doctor;
    }

    @Override
    public Optional<Doctor> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorMapper doctorMapper = session.getMapper(DoctorMapper.class);
            return Optional.ofNullable(doctorMapper.findById(id));
        }
    }

    @Override
    public Optional<Doctor> findByUserId(long userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorMapper doctorMapper = session.getMapper(DoctorMapper.class);
            return Optional.ofNullable(doctorMapper.findByUserId(userId));
        }
    }

    @Override
    public List<Doctor> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorMapper doctorMapper = session.getMapper(DoctorMapper.class);
            return doctorMapper.findAll();
        }
    }

    @Override
    public Doctor update(Doctor doctor) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorMapper doctorMapper = session.getMapper(DoctorMapper.class);
            doctorMapper.update(doctor);
            session.commit();
        }
        return doctor;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorMapper doctorMapper = session.getMapper(DoctorMapper.class);
            doctorMapper.delete(id);
            session.commit();
        }
    }
}
