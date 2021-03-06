package com.konstantinbulygin.pmwebapp.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_accounts_seq")
    @SequenceGenerator(name = "user_accounts_seq", sequenceName = "user_accounts_seq",
            allocationSize = 1,initialValue=1)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "username")
    @NotBlank(message = "")
    @Size(min = 3, message = "The name must be at least 3 characters long")
    private String userName;

    @NotBlank(message = "Please enter a valid email")
    @Email
    private String email;

    @Size(min = 4, message = "Password must be at least 4 characters long")
    @NotNull
    private String password;

    @Transient
    private String confirmPassword;

    @NotNull
    private String role = "ROLE_USER";

    private boolean enabled = true;

    public UserAccount() {
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
