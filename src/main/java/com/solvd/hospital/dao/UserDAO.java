package com.solvd.hospital.dao;

import com.solvd.hospital.entities.user.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> getById(long id);

    Optional<User> getByUsername(String username);

    User create(User user);

    User update(User user);

    void delete(long id);

    boolean isUsernameUnique(String username);
}
