package com.solvd.hospital.dao.jdbc.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.dao.DoctorDAO;
import com.solvd.hospital.entities.Doctor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCDoctorDAOImpl implements DoctorDAO {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_DOCTOR_WITH_USER_QUERY = "INSERT INTO doctors (first_name, last_name, specialization, user_id) " +
            "VALUES (?, ?, ?, ?)";
    private static final String CREATE_DOCTOR_QUERY = "INSERT INTO doctors (first_name, last_name, specialization) " +
            "VALUES (?, ?, ?)";
    private static final String FIND_ALL_DOCTORS_QUERY = "SELECT * FROM doctors";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM doctors WHERE id = ?";
    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM doctors user_id = ?";
    private static final String UPDATE_DOCTOR_QUERY = "UPDATE doctors " +
            "SET first_name = ?, last_name = ?, specialization = ? WHERE id = ?";
    private static final String UPDATE_DOCTOR_USER_ID_QUERY = "UPDATE doctors SET user_id = ? WHERE id = ?";
    private static final String DELETE_DOCTOR_BY_ID_QUERY = "DELETE FROM doctors WHERE id = ?";

    @Override
    public Doctor createWithUser(Doctor doctor) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(CREATE_DOCTOR_WITH_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getSpecialization());
            statement.setLong(4, doctor.getUserId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    doctor.setId(id);
                } else {
                    throw new SQLException("Creating doctor failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating doctor", e);
        }
        return doctor;
    }

    @Override
    public Doctor create(Doctor doctor) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement(CREATE_DOCTOR_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getSpecialization());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    doctor.setId(id);
                } else {
                    throw new SQLException("Creating doctor failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating doctor", e);
        }
        return doctor;
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_DOCTORS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                doctors.add(resultSetToDoctor(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all doctors", e);
        }
        return doctors;
    }

    @Override
    public Optional<Doctor> findById(long id) {
        return findById(id, FIND_BY_ID_QUERY);
    }

    @Override
    public Optional<Doctor> findByUserId(long id) {
        return findById(id, FIND_BY_USER_ID_QUERY);
    }

    @Override
    public Doctor update(Doctor doctor) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DOCTOR_QUERY)) {

            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getSpecialization());
            statement.setLong(4, doctor.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating doctor failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating doctor", e);
        }
        return doctor;
    }

    @Override
    public Doctor updateUserId(Doctor doctor) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DOCTOR_USER_ID_QUERY)) {

            statement.setLong(1, doctor.getUserId());
            statement.setLong(2, doctor.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating doctor failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating doctor", e);
        }
        return doctor;
    }

    @Override
    public void delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_DOCTOR_BY_ID_QUERY)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Doctor> findById(long id, String query) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToDoctor(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Doctor resultSetToDoctor(ResultSet resultSet) throws SQLException {
        return new Doctor()
                .setId(resultSet.getLong("id"))
                .setUserId(resultSet.getLong("user_id"))
                .setFirstName(resultSet.getString("first_name"))
                .setLastName(resultSet.getString("last_name"))
                .setSpecialization(resultSet.getString("specialization"));
    }

    private Doctor createDoctor(Doctor doctor, String query, Long userId) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getSpecialization());

            if (userId != null) {
                statement.setLong(4, userId);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    doctor.setId(id);
                } else {
                    throw new SQLException("Creating doctor failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating doctor", e);
        }
        return doctor;
    }
}
