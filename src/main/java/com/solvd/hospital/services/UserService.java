package com.solvd.hospital.services;

import com.solvd.hospital.dao.DAOFactory;
import com.solvd.hospital.common.PasswordHashing;
import com.solvd.hospital.common.ValidationUtils;
import com.solvd.hospital.common.exceptions.*;
import com.solvd.hospital.dao.UserDAO;
import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.user.User;

import java.util.Optional;

public class UserService {
    private final UserDAO dao;

    public UserService() {
        this.dao = DAOFactory.createUserDAO();
    }

    public User register(String username, String password, Role role) throws HospitalException {
        validateArgs(username, password);
        checkUsernameUniqueness(username);

        String hashedPassword = PasswordHashing.hashPassword(password);
        User user = new User()
                .setUsername(username)
                .setPassword(hashedPassword)
                .setRole(role);

        return dao.create(user);
    }

    public User login(String username, String password) throws HospitalException {
        validateArgs(username, password);
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

    private void checkUsernameUniqueness(String username) throws EntityAlreadyExistsException {
        if (!dao.isUsernameUnique(username)) {
            throw new EntityAlreadyExistsException("Username is not unique");
        }
    }

    private static void validateArgs(String username, String password) throws InvalidArgumentException {
        ValidationUtils.validateStringLength(username, "username", 45);
        ValidationUtils.validateStringLength(password, "password", 225);
    }
}
