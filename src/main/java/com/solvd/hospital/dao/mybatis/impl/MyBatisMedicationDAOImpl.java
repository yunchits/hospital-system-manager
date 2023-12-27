package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.MedicationDAO;
import com.solvd.hospital.dao.mybatis.mappers.MedicationMapper;
import com.solvd.hospital.entities.Medication;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisMedicationDAOImpl implements MedicationDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisMedicationDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Medication create(Medication medication) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MedicationMapper medicationMapper = session.getMapper(MedicationMapper.class);
            medicationMapper.create(medication);
            session.commit();
        }
        return medication;
    }

    @Override
    public Optional<Medication> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MedicationMapper medicationMapper = session.getMapper(MedicationMapper.class);
            return Optional.ofNullable(medicationMapper.findById(id));
        }
    }

    @Override
    public List<Medication> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MedicationMapper medicationMapper = session.getMapper(MedicationMapper.class);
            return medicationMapper.findAll();
        }
    }

    @Override
    public Medication update(Medication medication) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MedicationMapper medicationMapper = session.getMapper(MedicationMapper.class);
            medicationMapper.update(medication);
            session.commit();
        }
        return medication;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MedicationMapper medicationMapper = session.getMapper(MedicationMapper.class);
            medicationMapper.delete(id);
            session.commit();
        }
    }
}
