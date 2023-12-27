package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.PrescriptionDAO;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.dao.mybatis.mappers.PrescriptionMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisPrescriptionDAOImpl implements PrescriptionDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisPrescriptionDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Prescription create(Prescription prescription) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PrescriptionMapper prescriptionMapper = session.getMapper(PrescriptionMapper.class);
            prescriptionMapper.create(prescription);
            session.commit();
        }
        return prescription;
    }

    @Override
    public Optional<Prescription> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PrescriptionMapper prescriptionMapper = session.getMapper(PrescriptionMapper.class);
            return Optional.ofNullable(prescriptionMapper.findById(id));
        }
    }

    @Override
    public List<Prescription> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PrescriptionMapper prescriptionMapper = session.getMapper(PrescriptionMapper.class);
            return prescriptionMapper.findAll();
        }
    }

    @Override
    public List<Prescription> findByPatientId(long patientId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PrescriptionMapper prescriptionMapper = session.getMapper(PrescriptionMapper.class);
            return prescriptionMapper.findByPatientId(patientId);
        }
    }

    @Override
    public Prescription update(Prescription prescription) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PrescriptionMapper prescriptionMapper = session.getMapper(PrescriptionMapper.class);
            prescriptionMapper.update(prescription);
            session.commit();
        }
        return prescription;
    }

    @Override
    public void deleteByPatientId(long patientId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PrescriptionMapper prescriptionMapper = session.getMapper(PrescriptionMapper.class);
            prescriptionMapper.deleteByPatientId(patientId);
            session.commit();
        }
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PrescriptionMapper prescriptionMapper = session.getMapper(PrescriptionMapper.class);
            prescriptionMapper.delete(id);
            session.commit();
        }
    }
}
