package com.solvd.hospital.dao.mybatis.impl;

import com.solvd.hospital.common.MyBatisConfig;
import com.solvd.hospital.dao.BillDAO;
import com.solvd.hospital.dao.mybatis.mappers.BillMapper;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MyBatisBillDAOImpl implements BillDAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisBillDAOImpl() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    @Override
    public Bill create(Bill bill) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BillMapper billMapper = session.getMapper(BillMapper.class);
            billMapper.create(bill);
            session.commit();
        }
        return bill;
    }

    @Override
    public Optional<Bill> findById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BillMapper billMapper = session.getMapper(BillMapper.class);
            return Optional.ofNullable(billMapper.findById(id));
        }
    }

    @Override
    public List<Bill> findAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BillMapper billMapper = session.getMapper(BillMapper.class);
            return billMapper.findAll();
        }
    }

    @Override
    public List<Bill> findByPatientId(long patientId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BillMapper billMapper = session.getMapper(BillMapper.class);
            return billMapper.findByPatientId(patientId);
        }
    }

    @Override
    public List<Bill> findByPatientIdAndBillingDate(long patientId, LocalDate date) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BillMapper billMapper = session.getMapper(BillMapper.class);
            return billMapper.findByPatientIdAndBillingDate(patientId, date);
        }
    }

    @Override
    public List<Bill> findByPatientIdAndPaymentStatus(long patientId, PaymentStatus status) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BillMapper billMapper = session.getMapper(BillMapper.class);
            return billMapper.findByPatientIdAndPaymentStatus(patientId, status);
        }
    }

    @Override
    public Bill update(Bill bill) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BillMapper billMapper = session.getMapper(BillMapper.class);
            billMapper.update(bill);
            session.commit();
        }
        return bill;
    }

    @Override
    public void delete(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BillMapper billMapper = session.getMapper(BillMapper.class);
            billMapper.delete(id);
            session.commit();
        }
    }
}
