package com.solvd.hospital.services;

import com.solvd.hospital.dao.DAOFactory;
import com.solvd.hospital.common.ValidationUtils;
import com.solvd.hospital.common.exceptions.*;
import com.solvd.hospital.dao.InsuranceDAO;
import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.InsuranceType;

import java.time.LocalDate;
import java.util.List;

public class InsuranceService {

    private final InsuranceDAO dao;

    private final PatientService patientService;

    public InsuranceService() {
        this.dao = DAOFactory.createInsuranceDAO();
        this.patientService = new PatientService();
    }

    public Insurance create(long id,
                            String policyNumber,
                            LocalDate expirationDate,
                            double coverageAmount,
                            InsuranceType type,
                            String insuranceProvider) throws HospitalException {
        validateArgs(policyNumber, insuranceProvider);
        validateInsuranceDoesNotExist(id);
        validatePatientExists(id);

        return dao.create(new Insurance()
                .setPatientId(id)
                .setPolicyNumber(policyNumber)
                .setExpirationDate(expirationDate)
                .setCoverageAmount(coverageAmount)
                .setType(type)
                .setInsuranceProvider(insuranceProvider));
    }

    public List<Insurance> findAll() {
        return dao.findAll();
    }

    public Insurance findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Insurance with Patient ID " + id + " not found")
        );
    }

    public Insurance update(long id,
                            String policyNumber,
                            LocalDate expirationDate,
                            double coverageAmount,
                            InsuranceType type,
                            String insuranceProvider) throws HospitalException {
        validateArgs(policyNumber, insuranceProvider);
        findById(id);
        return dao.update(new Insurance()
                .setPatientId(id)
                .setPolicyNumber(policyNumber)
                .setExpirationDate(expirationDate)
                .setCoverageAmount(coverageAmount)
                .setType(type)
                .setInsuranceProvider(insuranceProvider));
    }

    private void validateInsuranceDoesNotExist(long id) throws DuplicateKeyException {
        if (dao.findById(id).isPresent()) {
            throw new DuplicateKeyException("Insurance with this Patient ID " + id + " already exists");
        }
    }

    private void validatePatientExists(long id) throws RelatedEntityNotFound {
        try {
            patientService.findById(id);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound(e.getMessage());
        }
    }

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }

    private static void validateArgs(String policyNumber, String insuranceProvider) throws InvalidArgumentException {
        ValidationUtils.validateStringLength(policyNumber, "policy number", 45);
        ValidationUtils.validateStringLength(insuranceProvider, "insurance provider name", 45);
    }
}
