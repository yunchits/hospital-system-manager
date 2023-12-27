package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.PatientDiagnosisDAO;
import com.solvd.hospital.dao.mybatis.mappers.PatientDiagnosisMapper;
import com.solvd.hospital.entities.PatientDiagnosis;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisPatientDiagnosisDAOImpl implements PatientDiagnosisDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisPatientDiagnosisDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public PatientDiagnosis create(PatientDiagnosis patientDiagnosis) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientDiagnosisMapper patientDiagnosisMapper = session.getMapper(PatientDiagnosisMapper.class);
            patientDiagnosisMapper.create(patientDiagnosis);
            session.commit();
        }
        return patientDiagnosis;
    }

    @Override
    public List<PatientDiagnosis> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientDiagnosisMapper patientDiagnosisMapper = session.getMapper(PatientDiagnosisMapper.class);
            return patientDiagnosisMapper.findAll();
        }
    }

    @Override
    public List<PatientDiagnosis> findAllByPatientId(long patientId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientDiagnosisMapper patientDiagnosisMapper = session.getMapper(PatientDiagnosisMapper.class);
            return patientDiagnosisMapper.findAllByPatientId(patientId);
        }
    }

    @Override
    public Optional<PatientDiagnosis> findByPatientIdAndDiagnosisId(long patientId, long diagnosisId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientDiagnosisMapper patientDiagnosisMapper = session.getMapper(PatientDiagnosisMapper.class);
            return patientDiagnosisMapper.findByPatientIdAndDiagnosisId(patientId, diagnosisId);
        }
    }

    @Override
    public PatientDiagnosis update(PatientDiagnosis patientDiagnosis, PatientDiagnosis newPatientDiagnosis) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientDiagnosisMapper patientDiagnosisMapper = session.getMapper(PatientDiagnosisMapper.class);
            patientDiagnosisMapper.update(patientDiagnosis, newPatientDiagnosis);
            session.commit();
        }
        return newPatientDiagnosis;
    }

    @Override
    public void delete(long patientId, long diagnosisId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PatientDiagnosisMapper patientDiagnosisMapper = session.getMapper(PatientDiagnosisMapper.class);
            patientDiagnosisMapper.delete(patientId, diagnosisId);
            session.commit();
        }
    }
}
