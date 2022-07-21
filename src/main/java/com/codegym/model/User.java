package com.codegym.model;

import com.codegym.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Accessors(chain = true)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String email;

    @Column
    private String address;

    @Column
    @Pattern(regexp = "^(\\+0?1\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$", message = "Wrong phone format. Please follow this pattern: +91 (123) 456-7890")
    private String phone;

    @Column
    private boolean isBlocked = false;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public UserDTO toUserDTO() {
        return new UserDTO()
                .setId(id)
                .setUsername(username)
                .setEmail(email)
                .setAddress(address)
                .setPhone(phone)
                .setBlocked(isBlocked)
                .setRole(role.toRoleDTO());
    }

    public User updateUser(UserDTO updates) {
        return this
                .setUsername(updates.getUsername())
                .setEmail(updates.getEmail())
                .setAddress(updates.getAddress())
                .setPhone(updates.getPhone())
                .setBlocked(updates.isBlocked())
                .setRole(updates.getRole().toRole());
    }
}