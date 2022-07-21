package com.codegym.service.role;

import com.codegym.model.Role;
import com.codegym.repository.IRoleRepository;
import com.codegym.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserService userService;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findOptById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public boolean ifExists(Long id) {
        return roleRepository.existsById(id);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role selectByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public boolean checkIfADMIN(Long id) {
        return userService.findById(id).getRole().getId() == 1;
    }
}