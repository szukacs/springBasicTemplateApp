package com.app.templateApp.service.user;

import com.app.templateApp.dto.user.NewUserDTO;
import com.app.templateApp.dto.user.registration.RegistrationDTO;
import com.app.templateApp.entity.user.User;
import com.app.templateApp.exception.user.*;
import com.app.templateApp.security.LoginAttemptService;
import com.app.templateApp.security.UserPrincipal;
import com.app.templateApp.util.FileConstant;
import com.app.templateApp.entity.user.Gender;

import com.app.templateApp.repository.UserRepository;
import com.app.templateApp.security.Role;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    public static final String USERNAME_ALREADY_EXISTS = "Username is already exists";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already exists";
    public static final String USER_NOT_FOUND_BY_USERNAME = "User not found by username: ";
    public static final String FOUND_USER_BY_USERNAME = "Returning found user by username: ";
    public static final String NO_USER_FOUND_BY_EMAIL = "Returning found user by email: ";

    private Logger LOGGER;
    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, ModelMapper modelMapper, LoginAttemptService loginAttemptService, Logger LOGGER) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.loginAttemptService = loginAttemptService;
        this.LOGGER = LOGGER;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username);
        if (user == null) {
            LOGGER.error(USER_NOT_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    @Override
    public User register(RegistrationDTO registrationDTO) throws UserManipulationException {
        registrationValidation(registrationDTO.getUserName(), registrationDTO.getEmail());
        User user = modelMapper.map(registrationDTO, User.class);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(registrationDTO.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setProfileImageUrl(getTemporaryProfileImageUrl(registrationDTO.getUserName()));
        userRepository.save(user);
        LOGGER.info("New user created: " + registrationDTO.getUserName());
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findUserByUserName(username);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        return user;
    }

    @Override
    public User addUser(NewUserDTO newUserDTO) throws UserManipulationException, IOException {
        registrationValidation(newUserDTO.getUserName(), newUserDTO.getEmail());
        User user = modelMapper.map(newUserDTO, User.class);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(newUserDTO.getPassword()));
        user.setProfileImageUrl(getTemporaryProfileImageUrl(newUserDTO.getUserName()));
        userRepository.save(user);
        saveProfileImg(user, newUserDTO.getProfileImageUrl());
        return user;
    }


    @Override
    public User findById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found with the provided id.");
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User can't be removed because user does not exist wit the provided id.");
        } else {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User updateProfileImg(Long id, MultipartFile profileImg) throws UserManipulationException, IOException {
        User user = findById(id);
        saveProfileImg(user, profileImg);
        return user;
    }

    @Override
    public void activateUser(Long id) throws UserNotFoundException {
        User user = findById(id);
        user.setActive(true);
        userRepository.save(user);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createFirstAdmin() {
        createUser("user", "user", Role.ROLE_USER);
        createUser("admin", "admin", Role.ROLE_SUPER_ADMIN);
    }

    public void createUser(String userName, String password, Role role) {
        User user = new User(userName, password, role, true);
        userRepository.save(user);
    }

    private void saveProfileImg(User user, MultipartFile profileImageUrl) throws IOException {
        if (profileImageUrl != null) {
            Path userFolder = Paths.get(FileConstant.USER_FOLDER + user.getUserName()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(FileConstant.DIRECTORY_CREATED + userFolder.toString());
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUserName() + FileConstant.DOT + FileConstant.JPG_EXTENSION));
            Files.copy(profileImageUrl.getInputStream(), userFolder.resolve(user.getUserName() + FileConstant.DOT + FileConstant.JPG_EXTENSION), StandardCopyOption.REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImgUrl(user.getUserName()));
            userRepository.save(user);
            LOGGER.info(FileConstant.FILE_SAVED_IN_FILE_SYSTEM + profileImageUrl.getOriginalFilename());
        }
    }

    private String setProfileImgUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.USER_IMAGE_PATH + username + FileConstant.FORWARD_SLASH
                + username + FileConstant.DOT + FileConstant.JPG_EXTENSION).toUriString();
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validateLoginAttempt(User user) {
        if (user.getLocked()) {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUserName());
        } else {
            if (loginAttemptService.hasExceededMaxAttempts(user.getUserName())) {
                user.setLocked(true);
            }
        }
    }

    private void registrationValidation(String username, String email) throws UsernameExistException, EmailExistException {
        if (StringUtils.isNoneBlank(username)) {
            User userByUserName = userRepository.findUserByUserName(username);
            User userByEmail = userRepository.findUserByEmail(email);
            if (userByUserName != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
        }
    }
}
