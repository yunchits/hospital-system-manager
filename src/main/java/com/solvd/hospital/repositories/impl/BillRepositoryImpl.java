package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import com.solvd.hospital.repositories.BillRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillRepositoryImpl implements BillRepository {
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_BILL_QUERY = "INSERT INTO bills (patient_id, amount, billing_date, payment_status) " +
        "VALUES (?, ?, ?, ?)";
    private static final String GET_BILLS_BY_PATIENT_ID_QUERY = "SELECT * FROM bills WHERE patient_id = ?";
    private static final String GET_BILLS_BY_PATIENT_ID_AND_DATE_QUERY = "SELECT * FROM bills " +
        "WHERE patient_id = ? AND billing_date = ?";
    private static final String GET_BILLS_BY_PATIENT_ID_AND_STATUS_QUERY = "SELECT * FROM bills " +
        "WHERE patient_id = ? AND payment_status = ?";
    private static final String UPDATE_BILL_QUERY = "UPDATE bills " +
        "SET amount = ?, billing_date = ?, payment_status = ?  WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM bills WHERE id = ?";

    @Override
    public Bill create(Bill bill) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement(CREATE_BILL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, bill.getPatientId());
            statement.setBigDecimal(2, BigDecimal.valueOf(bill.getAmount()));
            statement.setDate(3, Date.valueOf(bill.getBillingDate()));
            statement.setString(4, bill.getPaymentStatus().name());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating bill failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    bill.setId(id);
                } else {
                    throw new SQLException("Creating bill failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating bill", e);
        }
        return bill;
    }

    @Override
    public List<Bill> findByPatientId(long patientId) {
        List<Bill> bills = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BILLS_BY_PATIENT_ID_QUERY)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery();) {
                while (resultSet.next()) {
                    bills.add(resultSetToBill(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all bills.", e);
        }
        return bills;
    }

    @Override
    public List<Bill> findByPatientIdAndBillingDate(long patientId, LocalDate date) {
        List<Bill> bills = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BILLS_BY_PATIENT_ID_AND_DATE_QUERY)) {

            statement.setLong(1, patientId);
            statement.setDate(2, Date.valueOf(date));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bills.add(resultSetToBill(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all doctors.", e);
        }
        return bills;
    }

    @Override
    public List<Bill> findByPatientIdAndPaymentStatus(long patientId, PaymentStatus status) {
        List<Bill> bills = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BILLS_BY_PATIENT_ID_AND_STATUS_QUERY)) {

            statement.setLong(1, patientId);
            statement.setString(2, status.name());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bills.add(resultSetToBill(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all doctors.", e);
        }
        return bills;
    }

    @Override
    public Bill update(Bill bill) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BILL_QUERY)) {

            statement.setBigDecimal(1, BigDecimal.valueOf(bill.getAmount()));
            statement.setDate(2, Date.valueOf(bill.getBillingDate()));
            statement.setString(3, bill.getPaymentStatus().name());
            statement.setLong(4, bill.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating bill failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating bill salary", e);
        }
        return bill;
    }

    @Override
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting bill salary", e);
        }
    }

    private Bill resultSetToBill(ResultSet resultSet) throws SQLException {
        return new Bill()
            .setId(resultSet.getLong("id"))
            .setPatientId(resultSet.getLong("patient_id"))
            .setAmount(resultSet.getBigDecimal("amount").doubleValue())
            .setBillingDate(resultSet.getDate("billing_date").toLocalDate())
            .setPaymentStatus(PaymentStatus.valueOf(resultSet.getString("payment_status")));
    }
}
