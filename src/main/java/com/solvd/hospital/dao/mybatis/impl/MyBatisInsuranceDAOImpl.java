package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.InsuranceDAO;
import com.solvd.hospital.dao.mybatis.mappers.InsuranceMapper;
import com.solvd.hospital.entities.patient.Insurance;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisInsuranceDAOImpl implements InsuranceDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisInsuranceDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Insurance create(Insurance insurance) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            InsuranceMapper insuranceMapper = session.getMapper(InsuranceMapper.class);
            insuranceMapper.create(insurance);
            session.commit();
        }
        return insurance;
    }

    @Override
    public List<Insurance> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            InsuranceMapper insuranceMapper = session.getMapper(InsuranceMapper.class);
            return insuranceMapper.findAll();
        }
    }

    @Override
    public Optional<Insurance> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            InsuranceMapper insuranceMapper = session.getMapper(InsuranceMapper.class);
            return insuranceMapper.findById(id);
        }
    }

    @Override
    public Insurance update(Insurance insurance) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            InsuranceMapper insuranceMapper = session.getMapper(InsuranceMapper.class);
            insuranceMapper.update(insurance);
            session.commit();
        }
        return insurance;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            InsuranceMapper insuranceMapper = session.getMapper(InsuranceMapper.class);
            insuranceMapper.deleteByPatientId(id);
            session.commit();
        }
    }
}
