package com.app.templateApp.entity.user;

import com.app.templateApp.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(unique = true)
    private String userName;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private Integer age;
    private Gender gender = Gender.UNKNOWN;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private Role role;
    private Boolean active = false;
    private Boolean locked = false;

    public User(String userName, String password, Role role, Boolean active) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.active = active;
    }
}
