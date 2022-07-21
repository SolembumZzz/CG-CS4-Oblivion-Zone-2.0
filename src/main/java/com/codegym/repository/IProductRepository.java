package com.codegym.repository;

import com.codegym.model.Product;
import com.codegym.model.dto.ProductDTO;
import com.codegym.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    @Query("SELECT NEW com.codegym.model.dto.ProductDTO " +
            "(p.id, " +
            "p.name, " +
            "p.price, " +
            "p.quantity, " +
            "p.isLocked, " +
            "p.category, " +
            "p.createdBy) " +
            "FROM Product p")
    List<ProductDTO> findAllDTO();

    @Query("SELECT NEW com.codegym.model.dto.ProductDTO " +
            "(p.id, " +
            "p.name, " +
            "p.price, " +
            "p.quantity, " +
            "p.isLocked, " +
            "p.category, " +
            "p.createdBy) " +
            "FROM Product p " +
            "WHERE p.createdBy.id = ?1")
    List<ProductDTO> findAllOwnedProductsDTO(Long id);

    @Query("SELECT NEW com.codegym.model.dto.ProductDTO " +
            "(p.id, " +
            "p.name, " +
            "p.price, " +
            "p.quantity, " +
            "p.isLocked, " +
            "p.category, " +
            "p.createdBy) " +
            "FROM Product p " +
            "WHERE p.id = ?1")
    ProductDTO findDTOById(Long id);
}
