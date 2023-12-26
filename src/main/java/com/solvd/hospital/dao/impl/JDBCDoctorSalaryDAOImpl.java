package com.solvd.hospital.dao.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.doctor.DoctorSalary;
import com.solvd.hospital.dao.DoctorSalaryDAO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCDoctorSalaryDAOImpl implements DoctorSalaryDAO {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_SALARY_QUERY = "INSERT INTO doctor_salaries (salary, payment_date) " +
        "VALUES (?, ?)";
    private static final String FIND_ALL_SALARY_QUERY = "SELECT * FROM doctor_salaries";
    private static final String GET_SALARY_BY_ID_QUERY = "SELECT * FROM doctor_salaries WHERE id = ?";
    private static final String UPDATE_SALARY_QUERY = "UPDATE doctor_salaries SET salary = ?, payment_date = ? WHERE id = ?";
    private static final String DELETE_SALARY_QUERY = "DELETE FROM doctor_salaries WHERE id = ?";

    @Override
    public DoctorSalary create(DoctorSalary salary) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement(CREATE_SALARY_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setBigDecimal(1, BigDecimal.valueOf(salary.getSalary()));
            statement.setDate(2, Date.valueOf(salary.getPaymentDate()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor salary failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    salary.setId(id);
                } else {
                    throw new SQLException("Creating doctor salary failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating doctor salary", e);
        }
        return salary;
    }

    @Override
    public List<DoctorSalary> findAll() {
        List<DoctorSalary> salaries = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SALARY_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                salaries.add(resultSetToSalary(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all salaries.", e);
        }
        return salaries;
    }

    @Override
    public Optional<DoctorSalary> findById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_SALARY_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToSalary(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public DoctorSalary update(DoctorSalary salary) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SALARY_QUERY)) {

            statement.setBigDecimal(1, BigDecimal.valueOf(salary.getSalary()));
            statement.setDate(2, Date.valueOf(salary.getPaymentDate()));
            statement.setLong(3, salary.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating doctor salary failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating doctor salary", e);
        }
        return salary;
    }

    @Override
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SALARY_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting doctor salary", e);
        }
    }

    private DoctorSalary resultSetToSalary(ResultSet resultSet) throws SQLException {
        return new DoctorSalary()
            .setId(resultSet.getLong("id"))
            .setSalary(resultSet.getBigDecimal("salary").doubleValue())
            .setPaymentDate(resultSet.getDate("payment_date").toLocalDate());
    }
}
