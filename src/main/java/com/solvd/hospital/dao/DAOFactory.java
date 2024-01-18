package com.solvd.hospital.dao;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.dao.jdbc.impl.*;
import com.solvd.hospital.dao.mybatis.impl.*;

public final class DAOFactory {

    private DAOFactory() {
    }

    public static AppointmentDAO createAppointmentDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisAppointmentDAOImpl();
            case "jdbc" -> new JDBCAppointmentDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static BillDAO createBillDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisBillDAOImpl();
            case "jdbc" -> new JDBCBillDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static DiagnosisDAO createDiagnosisDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisDiagnosisDAOImpl();
            case "jdbc" -> new JDBCDiagnosisDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static DoctorDAO createDoctorDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisDoctorDAOImpl();
            case "jdbc" -> new JDBCDoctorDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static HospitalizationDAO createHospitalizationDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisHospitalizationDAOImpl();
            case "jdbc" -> new JDBCHospitalizationDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static InsuranceDAO createInsuranceDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisInsuranceDAOImpl();
            case "jdbc" -> new JDBCInsuranceDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static MedicationDAO createMedicationDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisMedicationDAOImpl();
            case "jdbc" -> new JDBCMedicationDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static PatientDiagnosisDAO createPatientDiagnosisDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisPatientDiagnosisDAOImpl();
            case "jdbc" -> new JDBCPatientDiagnosisDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static PatientDAO createPatientDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisPatientDAOImpl();
            case "jdbc" -> new JDBCPatientDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static PrescriptionDAO createPrescriptionDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisPrescriptionDAOImpl();
            case "jdbc" -> new JDBCPrescriptionDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    public static UserDAO createUserDAO() {
        return switch (getProperty()) {
            case "mybatis" -> new MyBatisUserDAOImpl();
            case "jdbc" -> new JDBCUserDAOImpl();
            default -> throw new IllegalArgumentException("Invalid DAO type");
        };
    }

    private static String getProperty() {
        return AppProperties.getProperty("dao.type");
    }
}
