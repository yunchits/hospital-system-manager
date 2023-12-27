package com.solvd.hospital.apps;

import com.solvd.hospital.dao.HospitalizationDAO;
import com.solvd.hospital.dao.mybatis.impl.MyBatisDiagnosisDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisHospitalizationDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisInsuranceDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisPatientDiagnosisDAOImpl;

public class Test {

    public static void main(String[] args) {
//        DoctorSalaryDAO salaryDAO = new MyBatisDoctorSalaryDAOImpl();
//
//        System.out.println(salaryDAO.findAll());
//
//        DoctorDAO doctorDAO = new MyBatisDoctorDAOImpl();
//
//        System.out.println(doctorDAO.findAll());
//
//        MyBatisPatientDAOImpl patientDAO = new MyBatisPatientDAOImpl();
//
//        System.out.println(patientDAO.findAll());
//
//        MyBatisMedicationDAOImpl medicationDAO = new MyBatisMedicationDAOImpl();
//
//        System.out.println(medicationDAO.findAll());
//
//        MyBatisPrescriptionDAOImpl prescriptionDAO = new MyBatisPrescriptionDAOImpl();
//
//        System.out.println(prescriptionDAO.findAll());
//
//        MyBatisAppointmentDAOImpl appointmentDAO = new MyBatisAppointmentDAOImpl();
//
//        System.out.println(appointmentDAO.findAll());
//
//        MyBatisBillDAOImpl billDAO = new MyBatisBillDAOImpl();
//
//        System.out.println(billDAO.findAll());

        MyBatisDiagnosisDAOImpl diagnosisDAO = new MyBatisDiagnosisDAOImpl();

        System.out.println(diagnosisDAO.findAll());

        System.out.println(diagnosisDAO.findById(1L));

        MyBatisPatientDiagnosisDAOImpl patientDiagnosisDAO = new MyBatisPatientDiagnosisDAOImpl();

        System.out.println(patientDiagnosisDAO.findAll());

        MyBatisHospitalizationDAOImpl hospitalizationDAO = new MyBatisHospitalizationDAOImpl();
        System.out.println(hospitalizationDAO.findAll());

        MyBatisInsuranceDAOImpl insuranceDAO = new MyBatisInsuranceDAOImpl();

        System.out.println(insuranceDAO.findAll());
    }
}
