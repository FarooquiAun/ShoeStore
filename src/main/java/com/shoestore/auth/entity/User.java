package com.shoestore.auth.entity;

import com.shoestore.common.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User(LocalDateTime createdAt, String email, Long id, boolean isActive, String password, Role role, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.email = email;
        this.id = id;
        this.isActive = isActive;
        this.password = password;
        this.role = role;
        this.updatedAt = updatedAt;
    }

    public User() {
    }
    private User(Builder builder){
        this.email=builder.email;
        this.password=builder.password;
        this.role=builder.role;
        this.isActive= builder.isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    @PrePersist
    public  void  prePersist(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }
    @PostPersist
    public void  postPersist(){
        this.updatedAt=LocalDateTime.now();
    }


    public static class Builder{
        private String email;
        private String password;
        private Role role;
        private boolean isActive;

        public Builder email(String email){
            this.email=email;
            return this;
        }
        public Builder password(String password){
            this.password=password;
            return this;
        }
        public Builder role(Role role){
            this.role=role;
            return this;
        }
        public Builder isActive(boolean isActive){
            this.isActive=isActive;
            return this;
        }
        public User build(){
            return new User(this);
        }


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }
    }
    public static Builder builder() {
        return new Builder();
    }


}
