package com.solvd.hospital;

import com.solvd.hospital.common.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        ConnectionPool pool = ConnectionPool.getInstance();

        try (Connection connection = pool.getConnection()) {
            String sql = "INSERT INTO patients (first_name, last_name, birth_date, gender) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "John");
                preparedStatement.setString(2, "Doe");
                preparedStatement.setString(3, "1990-01-01");
                preparedStatement.setString(4, "MALE");

                preparedStatement.executeUpdate();

                System.out.println("Patient added successfully.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
