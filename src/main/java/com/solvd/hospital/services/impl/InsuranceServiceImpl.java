package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.DuplicateKeyException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.dao.InsuranceDAO;
import com.solvd.hospital.dao.impl.JDBCInsuranceDAOImpl;
import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.InsuranceType;
import com.solvd.hospital.services.InsuranceService;
import com.solvd.hospital.services.PatientService;

import java.time.LocalDate;
import java.util.List;

public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceDAO dao;

    private final PatientService patientService;

    public InsuranceServiceImpl() {
        this.dao = new JDBCInsuranceDAOImpl();
        this.patientService = new PatientServiceImpl();
    }

    @Override
    public Insurance create(long id,
                            String policyNumber,
                            LocalDate expirationDate,
                            double coverageAmount,
                            InsuranceType type,
                            String insuranceProvider) throws DuplicateKeyException, RelatedEntityNotFound {
        validateInsuranceDoesNotExist(id);
        validatePatientExists(id);

        return dao.create(new Insurance()
            .setPolicyNumber(policyNumber)
            .setExpirationDate(expirationDate)
            .setCoverageAmount(coverageAmount)
            .setType(type)
            .setInsuranceProvider(insuranceProvider));
    }

    @Override
    public List<Insurance> findAll() {
        return dao.findAll();
    }


    @Override
    public Insurance findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Insurance with Patient ID " + " not found")
        );
    }

    @Override
    public Insurance update(long id,
                            String policyNumber,
                            LocalDate expirationDate,
                            double coverageAmount,
                            InsuranceType type,
                            String insuranceProvider) throws EntityNotFoundException {
        validateInsuranceExists(id);

        return dao.update(new Insurance()
            .setPatientId(id)
            .setPolicyNumber(policyNumber)
            .setExpirationDate(expirationDate)
            .setCoverageAmount(coverageAmount)
            .setType(type)
            .setInsuranceProvider(insuranceProvider));
    }

    private void validateInsuranceExists(long id) throws EntityNotFoundException {
        if (dao.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Insurance with this Patient ID " + id + " not found");
        }
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

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Insurance with this Patient ID " + id + " already exists");
        }
    }
}
