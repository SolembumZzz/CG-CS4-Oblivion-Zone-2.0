package com.codegym.service.product;

import com.codegym.model.Category;
import com.codegym.service.IGeneralService;

import java.util.List;

public interface ICategoryService extends IGeneralService<Category> {
    List<Category> findAll();

    Category create(Category newCategory);

    Category update(Category updatedCategory);
}
