package com.codegym.service.product;

import com.codegym.model.Category;
import com.codegym.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    ICategoryRepository categoryRepository;

    @Override
    public Optional<Category> findOptById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category findById(Long id) {
        return findOptById(id).get();
    }

    @Override
    public boolean ifExists(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category create(Category newCategory) {
        return save(newCategory);
    }

    @Override
    public Category update(Category updatedCategory) {
        return save(updatedCategory);
    }
}
