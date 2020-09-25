package com.app.templateApp.repository;

import com.app.templateApp.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserName(String username);

    User findUserByEmail(String email);
}
