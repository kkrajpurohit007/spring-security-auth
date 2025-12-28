package org.finora.app.auth.service;

import org.finora.app.auth.dto.CreateUserRequest;
import org.finora.app.auth.dto.UpdateUserRequest;
import org.finora.app.auth.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);
}
