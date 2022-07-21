package com.codegym.service.product;

import com.codegym.model.Product;
import com.codegym.model.dto.ProductDTO;
import com.codegym.service.IGeneralService;

import java.util.List;

public interface IProductService extends IGeneralService<Product> {
    List<ProductDTO> findAllDTO();

    List<ProductDTO> findAllOwnedProductsDTO(Long id);

    Product findByName(String name);

    ProductDTO findDTOById(Long id);

    boolean checkIfProductIsRightfullyOwned(Long productId, Long ownerId);

    Product create(ProductDTO newProduct);

    Product update(ProductDTO updates);

    Product lock(Long id);

    Product unlock(Long id);

    boolean ifLocked(Long id);
}
