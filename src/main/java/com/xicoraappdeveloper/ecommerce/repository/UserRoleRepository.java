package com.xicoraappdeveloper.ecommerce.repository;

import com.xicoraappdeveloper.ecommerce.entity.UserRole;
import com.xicoraappdeveloper.ecommerce.entity.UserRole.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    void deleteByIdUserId(Long userId);
}
