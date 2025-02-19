package com.xicoraappdeveloper.ecommerce.service;

import com.xicoraappdeveloper.ecommerce.model.dto.request.UserRegisterRequest;
import com.xicoraappdeveloper.ecommerce.model.dto.request.UserUpdateRequest;
import com.xicoraappdeveloper.ecommerce.model.dto.response.UserResponse;

import javax.management.relation.RoleNotFoundException;

public interface UserService {

    UserResponse register(UserRegisterRequest registerRequest);

    UserResponse findById(Long id);

    UserResponse findByKeyword(String keyword);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
