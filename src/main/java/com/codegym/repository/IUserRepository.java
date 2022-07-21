package com.codegym.repository;

import com.codegym.model.User;
import com.codegym.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<User> findByUsername(String username);

    @Query("SELECT NEW com.codegym.model.dto.UserDTO (u.id, u.username, u.email, u.address, u.phone, u.isBlocked, u.role) FROM User u")
    List<UserDTO> findAllDTO();

    @Query("SELECT NEW com.codegym.model.dto.UserDTO (u.id, u.username, u.email, u.address, u.phone, u.isBlocked, u.role) FROM User u WHERE u.role.id = 1")
    List<UserDTO> findAllUSERSDTO();

    @Query("SELECT NEW com.codegym.model.dto.UserDTO (u.id, u.username, u.email, u.address, u.phone, u.isBlocked, u.role) FROM User u WHERE u.id = ?1")
    UserDTO selectUserDTOById(Long id);

    @Query("SELECT NEW com.codegym.model.dto.UserDTO (u.id, u.username, u.password, u.email, u.address, u.phone, u.isBlocked, u.role) FROM User u WHERE u.username = ?1")
    Optional<UserDTO> findUserDTOByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdIsNot(String phone, Long id);
}