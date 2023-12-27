package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.PatientDAO;
import com.solvd.hospital.dao.mybatis.mappers.PatientMapper;
import com.solvd.hospital.entities.patient.Patient;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisPatientDAOImpl implements PatientDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisPatientDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Patient create(Patient patient) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientMapper patientMapper = session.getMapper(PatientMapper.class);
            patientMapper.create(patient);
            session.commit();
        }
        return patient;
    }

    @Override
    public Optional<Patient> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientMapper patientMapper = session.getMapper(PatientMapper.class);
            return Optional.ofNullable(patientMapper.findById(id));
        }
    }

    @Override
    public List<Patient> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientMapper patientMapper = session.getMapper(PatientMapper.class);
            return patientMapper.findAll();
        }
    }

    @Override
    public Patient update(Patient patient) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientMapper patientMapper = session.getMapper(PatientMapper.class);
            patientMapper.update(patient);
            session.commit();
        }
        return patient;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientMapper patientMapper = session.getMapper(PatientMapper.class);
            patientMapper.delete(id);
            session.commit();
        }
    }
}
