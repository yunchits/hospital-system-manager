package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.DiagnosisDAO;
import com.solvd.hospital.dao.mybatis.mappers.DiagnosisMapper;
import com.solvd.hospital.entities.Diagnosis;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisDiagnosisDAOImpl implements DiagnosisDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisDiagnosisDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Diagnosis create(Diagnosis diagnosis) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            diagnosisMapper.create(diagnosis);
            session.commit();
        }
        return diagnosis;
    }

    @Override
    public Optional<Diagnosis> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            return diagnosisMapper.findById(id);
        }
    }

    @Override
    public List<Diagnosis> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            return diagnosisMapper.findAll();
        }
    }

    @Override
    public Optional<Diagnosis> findByDiagnosisName(String name) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            return diagnosisMapper.findByDiagnosisName(name);
        }
    }

    @Override
    public Diagnosis update(Diagnosis diagnosis) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            diagnosisMapper.update(diagnosis);
            session.commit();
        }
        return diagnosis;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            diagnosisMapper.delete(id);
            session.commit();
        }
    }

    @Override
    public boolean isDiagnosisUnique(String name) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DiagnosisMapper diagnosisMapper = session.getMapper(DiagnosisMapper.class);
            return diagnosisMapper.isDiagnosisUnique(name);
        }
    }
}
