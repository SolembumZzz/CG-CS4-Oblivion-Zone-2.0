package com.codegym.service.user;

import com.codegym.model.User;
import com.codegym.model.dto.UserDTO;
import com.codegym.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    List<UserDTO> findAllDTO();

    List<UserDTO> findALlUSERSDTO();

    User findByUsername(String username);

    UserDTO findDTOById(Long id);

    Optional<UserDTO> findUserDTOByUsername(String username);

    User create(UserDTO newUser);

    User update(UserDTO updates);

    User block(Long id);

    User unblock(Long id);

    boolean ifEmailExists(String email);

    boolean ifEmailExistsExceptSelf(String email, Long id);

    boolean ifPhoneExists(String phone);

    boolean ifPhoneExistsExceptSelf(String phone, Long id);

    boolean ifBlocked(Long id);
}
