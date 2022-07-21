package com.codegym.service.product;

import com.codegym.model.Product;
import com.codegym.model.dto.ProductDTO;
import com.codegym.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{
    @Autowired
    IProductRepository productRepository;
    
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDTO> findAllOwnedProductsDTO(Long id) {
        return productRepository.findAllOwnedProductsDTO(id);
    }

    @Override
    public Optional<Product> findOptById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product findById(Long id) {
        return findOptById(id).get();
    }

    @Override
    public boolean ifExists(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean checkIfProductIsRightfullyOwned(Long productId, Long ownerId) {
        return findById(productId).getCreatedBy().getId().equals(ownerId);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<ProductDTO> findAllDTO() {
        return productRepository.findAllDTO();
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public ProductDTO findDTOById(Long id) {
        return productRepository.findDTOById(id);
    }

    @Override
    public Product create(ProductDTO newProduct) {
        Product product = newProduct.toProduct();
        return productRepository.save(product);
    }

    @Override
    public Product update(ProductDTO updates) {
        Product oldProduct = findById(updates.getId());
        oldProduct.updateProduct(updates);
        return productRepository.save(oldProduct);
    }

    @Override
    public Product lock(Long id) {
        Product product = findById(id);
        product.setLocked(true);
        return productRepository.save(product);
    }

    @Override
    public Product unlock(Long id) {
        Product product = findById(id);
        product.setLocked(false);
        return productRepository.save(product);
    }

    @Override
    public boolean ifLocked(Long id) {
        return (findById(id).isLocked());
    }
}
