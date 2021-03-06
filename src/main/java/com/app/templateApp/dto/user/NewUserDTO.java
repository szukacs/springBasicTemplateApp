package com.app.templateApp.dto.user;

import com.app.templateApp.entity.user.Gender;
import com.app.templateApp.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDTO {
    private String firstName;
    private String lastName;
    private Integer age;
    private Gender gender;
    private String userName;
    private String password;
    private String email;
    private MultipartFile profileImageUrl;
    private Role role;
    private Boolean active;
}
