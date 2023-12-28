package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.HospitalizationDAO;
import com.solvd.hospital.dao.mybatis.mappers.HospitalizationMapper;
import com.solvd.hospital.entities.Hospitalization;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisHospitalizationDAOImpl implements HospitalizationDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisHospitalizationDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Hospitalization create(Hospitalization hospitalization) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HospitalizationMapper hospitalizationMapper = session.getMapper(HospitalizationMapper.class);
            hospitalizationMapper.create(hospitalization);
            session.commit();
        }
        return hospitalization;
    }

    @Override
    public List<Hospitalization> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HospitalizationMapper hospitalizationMapper = session.getMapper(HospitalizationMapper.class);
            return hospitalizationMapper.findAll();
        }
    }

    @Override
    public List<Hospitalization> findByPatientId(long patientId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HospitalizationMapper hospitalizationMapper = session.getMapper(HospitalizationMapper.class);
            return hospitalizationMapper.findByPatientId(patientId);
        }
    }

    @Override
    public Optional<Hospitalization> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HospitalizationMapper hospitalizationMapper = session.getMapper(HospitalizationMapper.class);
            return hospitalizationMapper.findById(id);
        }
    }

    @Override
    public Hospitalization update(Hospitalization hospitalization) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HospitalizationMapper hospitalizationMapper = session.getMapper(HospitalizationMapper.class);
            hospitalizationMapper.update(hospitalization);
            session.commit();
        }
        return hospitalization;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            HospitalizationMapper hospitalizationMapper = session.getMapper(HospitalizationMapper.class);
            hospitalizationMapper.delete(id);
            session.commit();
        }
    }
}
