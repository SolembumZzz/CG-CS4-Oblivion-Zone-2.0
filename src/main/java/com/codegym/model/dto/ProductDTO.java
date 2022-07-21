package com.codegym.model.dto;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 5, max = 30, message = "Name's length must be between 5 and 30 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Range(min = 0, max = 1000000000, message = "Price must be between 0 and 1.000.000.000")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Range(min = 0, max = 5000000, message = "Quantity must be between 0 and 5.000.000")
    private int quantity;

    private boolean isLocked = false;

    private Category category;

    private UserDTO createdBy;

    public ProductDTO(Long id, String name, BigDecimal price, int quantity, boolean isLocked, Category category, User createdBy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isLocked = isLocked;
        this.category = category;
        this.createdBy = createdBy.toUserDTO();
    }

    public Product toProduct() {
        return new Product()
                .setId(id)
                .setName(name)
                .setPrice(price)
                .setQuantity(quantity)
                .setLocked(isLocked)
                .setCategory(category);
    }
}
