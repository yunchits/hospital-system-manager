package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.PatientDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCPatientDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisPatientDAOImpl;
import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.entities.user.User;

import java.time.LocalDate;
import java.util.List;

public class PatientService {

    private final PatientDAO dao;

    private final UserService userService;

    public PatientService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisPatientDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCPatientDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
        this.userService = new UserService();
    }

    public Patient createWithUser(String firstName,
                                  String lastName,
                                  LocalDate birthDate,
                                  Gender gender,
                                  String username,
                                  String password) throws EntityAlreadyExistsException {
        User user = userService.register(username, password, Role.PATIENT);

        return dao.createWithUser(new Patient()
                .setUserId(user.getId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setBirthDate(birthDate)
                .setGender(gender));
    }

    public Patient create(String firstName,
                                  String lastName,
                                  LocalDate birthDate,
                                  Gender gender) {
        return dao.create(new Patient()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setBirthDate(birthDate)
                .setGender(gender));
    }

    public List<Patient> findAll() {
        return dao.findAll();
    }

    public Patient findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Patient with id: " + id + " not found")
        );
    }

    public Patient findByUserId(long userId) throws EntityNotFoundException {
        return dao.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Patient with user id: " + userId + " not found")
        );
    }

    public Patient update(long id, String firstName, String lastName, LocalDate birthDate, Gender gender) throws EntityNotFoundException {
        Patient patient = new Patient();

        findById(id);

        patient.setId(id);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setBirthDate(birthDate);
        patient.setGender(gender);

        return dao.update(patient);
    }

    public Patient updateUserId(long id, long userId) throws EntityNotFoundException {
        Patient patient = findById(id);
        return dao.updateUserId(patient.setUserId(userId));
    }

    public void delete(long id) throws EntityNotFoundException {
        Patient patient = findById(id);
        dao.delete(id);
        userService.delete(patient.getUserId());
    }
}
