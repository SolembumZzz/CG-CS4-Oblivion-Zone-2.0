package com.codegym.service;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {
    List<T> findAll();

    Optional<T> findOptById(Long id);

    T findById(Long id);

    boolean ifExists(Long id);

    T save(T t);
}