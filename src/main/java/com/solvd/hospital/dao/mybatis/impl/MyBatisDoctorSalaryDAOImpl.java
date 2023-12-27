package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.DoctorSalaryDAO;
import com.solvd.hospital.entities.doctor.DoctorSalary;
import com.solvd.hospital.dao.mybatis.mappers.DoctorSalaryMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class MyBatisDoctorSalaryDAOImpl implements DoctorSalaryDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisDoctorSalaryDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public DoctorSalary create(DoctorSalary doctorSalary) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorSalaryMapper doctorSalaryMapper = session.getMapper(DoctorSalaryMapper.class);
            doctorSalaryMapper.create(doctorSalary);
            session.commit();
        }
        return doctorSalary;
    }

    @Override
    public Optional<DoctorSalary> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorSalaryMapper doctorSalaryMapper = session.getMapper(DoctorSalaryMapper.class);
            return Optional.ofNullable(doctorSalaryMapper.findById(id));
        }
    }

    @Override
    public List<DoctorSalary> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorSalaryMapper doctorSalaryMapper = session.getMapper(DoctorSalaryMapper.class);
            return doctorSalaryMapper.findAll();
        }
    }

    @Override
    public DoctorSalary update(DoctorSalary doctorSalary) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorSalaryMapper doctorSalaryMapper = session.getMapper(DoctorSalaryMapper.class);
            doctorSalaryMapper.update(doctorSalary);
            session.commit();
        }
        return doctorSalary;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DoctorSalaryMapper doctorSalaryMapper = session.getMapper(DoctorSalaryMapper.class);
            doctorSalaryMapper.delete(id);
            session.commit();
        }
    }
}
