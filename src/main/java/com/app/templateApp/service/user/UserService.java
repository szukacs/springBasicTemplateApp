package com.app.templateApp.service.user;

import com.app.templateApp.entity.user.User;
import com.app.templateApp.exception.user.EmailNotFoundException;
import com.app.templateApp.exception.user.UserManipulationException;
import com.app.templateApp.exception.user.UserNotFoundException;
import com.app.templateApp.dto.user.NewUserDTO;
import com.app.templateApp.dto.user.registration.RegistrationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    User register(RegistrationDTO registrationDTO) throws UserManipulationException;

    List<User> getUsers();

    User findByUsername(String username) throws UserNotFoundException;

    User findByEmail(String email) throws EmailNotFoundException;

    User addUser(NewUserDTO newUserDTO) throws UserManipulationException, IOException;

    User findById(Long id) throws UserNotFoundException;

    void deleteUser(Long id) throws UserNotFoundException;

    User updateProfileImg(Long id, MultipartFile profileImg) throws UserManipulationException, IOException;

    void activateUser(Long id) throws UserNotFoundException;
}
