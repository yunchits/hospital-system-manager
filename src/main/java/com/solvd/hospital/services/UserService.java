package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.PasswordHashing;
import com.solvd.hospital.common.exceptions.AuthenticationException;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.UsersDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCUserDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisUserDAOImpl;
import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.user.User;

import java.nio.file.LinkOption;
import java.util.Optional;

public class UserService {
    private final UsersDAO dao;

    public UserService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisUserDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCUserDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
    }

    public User register(String username, String password, Role role) throws EntityAlreadyExistsException {
        if (dao.getByUsername(username).isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        String hashedPassword = PasswordHashing.hashPassword(password);
        User user = new User()
                .setUsername(username)
                .setPassword(hashedPassword)
                .setRole(role);

        return dao.create(user);
    }

    public User login(String username, String password) throws AuthenticationException {
        Optional<User> userOptional = dao.getByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (PasswordHashing.verifyPassword(password, user.getPassword())) {
                return user;
            }
        }

        throw new AuthenticationException("Invalid username or password");
    }

    public User getById(long id) throws EntityNotFoundException {
        return dao.getById(id).orElseThrow(
                () -> new EntityNotFoundException("No User with this ID")
        );
    }

    public void delete(long id) {
        dao.getById(id);
        dao.delete(id);
    }
}
