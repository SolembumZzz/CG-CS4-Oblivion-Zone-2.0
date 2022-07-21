package com.codegym.service.user;

import com.codegym.model.User;
import com.codegym.model.UserPrinciple;
import com.codegym.model.dto.UserDTO;
import com.codegym.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> findALlUSERSDTO() {
        return userRepository.findAllUSERSDTO();
    }

    @Override
    public List<UserDTO> findAllDTO() {
        return userRepository.findAllDTO();
    }

    @Override
    public Optional<User> findOptById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public UserDTO findDTOById(Long id) {
        return userRepository.selectUserDTOById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public Optional<UserDTO> findUserDTOByUsername(String username) {
        return userRepository.findUserDTOByUsername(username);
    }

    @Override
    public boolean ifExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean ifEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean ifEmailExistsExceptSelf(String email, Long id) {
        return userRepository.existsByEmailAndIdIsNot(email, id);
    }

    @Override
    public boolean ifPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public boolean ifPhoneExistsExceptSelf(String phone, Long id) {
        return userRepository.existsByPhoneAndIdIsNot(phone, id);
    }

    @Override
    public boolean ifBlocked(Long id) {
        User user = findById(id);
        return user.isBlocked();
    }

    @Override
    public User create(UserDTO newUser) {
        User user = newUser.toUser();
        return save(user);
    }

    @Override
    public User update(UserDTO updates) {
        User oldUser = findOptById(updates.getId()).get();
        oldUser.updateUser(updates);
        return save(oldUser);
    }

    @Override
    public User block(Long id) {
        User user = findById(id);
        user.setBlocked(true);
        return save(user);
    }

    @Override
    public User unblock(Long id) {
        User user = findById(id);
        user.setBlocked(false);
        return save(user);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrinciple.build(userOptional.get());
//        return (UserDetails) userOptional.get();
    }

}