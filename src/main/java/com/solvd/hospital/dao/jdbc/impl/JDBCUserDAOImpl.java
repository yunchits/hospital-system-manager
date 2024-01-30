package com.solvd.hospital.dao.jdbc.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.common.exceptions.DataAccessException;
import com.solvd.hospital.dao.UserDAO;
import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class JDBCUserDAOImpl implements UserDAO {
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_USER_QUERY = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String GET_USER_BY_USERNAME_QUERY = "SELECT * FROM users WHERE username = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String COUNT_USERS_BY_USERNAME = "SELECT COUNT(*) FROM users WHERE username = ?";

    @Override
    public User create(User user) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_USER_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    user.setId(id);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating user", e);
        }
        return user;
    }

    @Override
    public Optional<User> getById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToUser(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getByUsername(String username) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_USERNAME_QUERY)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToUser(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return Optional.empty();
    }

    @Override
    public User update(User user) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_QUERY)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.setLong(4, user.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating user", e);
        }
        return user;
    }

    @Override
    public void delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user", e);
        }
    }

    @Override
    public boolean isUsernameUnique(String username) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_USERS_BY_USERNAME)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0;
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error checking username uniqueness", e);
        }
        return false;
    }

    private User resultSetToUser(ResultSet resultSet) throws SQLException {
        return new User()
                .setId(resultSet.getInt("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRole(Role.valueOf(resultSet.getString("role")));
    }
}
