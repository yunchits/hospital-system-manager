package com.solvd.hospital.services;

import com.solvd.hospital.dao.DAOFactory;
import com.solvd.hospital.common.ValidationUtils;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.HospitalException;
import com.solvd.hospital.common.exceptions.InvalidArgumentException;
import com.solvd.hospital.dao.DoctorDAO;
import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.user.User;

import java.util.List;

public class DoctorService {

    private final DoctorDAO dao;
    private final UserService userService;

    public DoctorService() {
        this.dao = DAOFactory.createDoctorDAO();
        this.userService = new UserService();
    }

    public Doctor create(String firstName,
                         String lastName,
                         String specialization) {
        return dao.create(new Doctor()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setSpecialization(specialization));
    }

    public Doctor createWithUser(String firstName,
                                 String lastName,
                                 String specialization,
                                 String username,
                                 String password) throws HospitalException {
        validateArgs(firstName, lastName, specialization);
        User user = userService.register(username, password, Role.DOCTOR);
        return dao.createWithUser(new Doctor()
                .setUserId(user.getId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setSpecialization(specialization));
    }

    public Doctor createWithUser(String firstName,
                                 String lastName,
                                 String specialization,
                                 long userId) throws HospitalException {
        validateArgs(firstName, lastName, specialization);
        User user = userService.getById(userId);
        return dao.createWithUser(new Doctor()
                .setUserId(user.getId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setSpecialization(specialization));
    }

    public List<Doctor> findAll() {
        return dao.findAll();
    }

    public Doctor findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Doctor with id: " + id + " not found")
        );
    }

    public Doctor findByUserId(long userId) throws EntityNotFoundException {
        return dao.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Doctor with user id: " + userId + " not found")
        );
    }

    public Doctor update(long id,
                         String firstName,
                         String lastName,
                         String specialization) throws HospitalException {
        validateArgs(firstName, lastName, specialization);
        findById(id);
        return dao.update(new Doctor()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setSpecialization(specialization));
    }

    public Doctor updateUserId(long id, long userId) throws EntityNotFoundException {
        Doctor doctor = findById(id);
        return dao.updateUserId(doctor.setUserId(userId));
    }

    public void delete(long id) throws EntityNotFoundException {
        Doctor doctor = findById(id);
        dao.delete(id);
        userService.delete(doctor.getUserId());
    }

    private static void validateArgs(String firstName, String lastName, String specialization) throws InvalidArgumentException {
        ValidationUtils.validateStringLength(firstName, "first name", 45);
        ValidationUtils.validateStringLength(lastName, "last name", 45);
        ValidationUtils.validateStringLength(specialization, "specialization", 45);
    }
}
