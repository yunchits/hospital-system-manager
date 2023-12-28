package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.DoctorDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCDoctorDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisDoctorDAOImpl;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.user.User;

import java.util.List;

public class DoctorService {

    private final DoctorDAO dao;
    private final UserService userService;

    public DoctorService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisDoctorDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCDoctorDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
        this.userService = new UserService();
    }

    public Doctor create(String firstName,
                         String lastName,
                         String specialization,
                         String username,
                         String password) throws EntityAlreadyExistsException {
        User user = userService.register(username, password, Role.DOCTOR);
        return dao.create(new Doctor()
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
                         String specialization) throws EntityNotFoundException {
        findById(id);

        return dao.update(new Doctor()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setSpecialization(specialization));
    }

    public void delete(long id) throws EntityNotFoundException {
        Doctor doctor = findById(id);
        dao.delete(id);
        userService.delete(doctor.getUserId());
    }
}
