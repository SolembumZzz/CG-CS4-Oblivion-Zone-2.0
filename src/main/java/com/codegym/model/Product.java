package com.codegym.model;

import com.codegym.model.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Accessors(chain = true)
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @Column
    private boolean isLocked = false;

    @ManyToOne
    @JoinColumn(nullable = false, name = "category_id", referencedColumnName = "id")
    private Category category;

    public ProductDTO toProductDTO() {
        return new ProductDTO()
                .setId(id)
                .setName(name)
                .setPrice(price)
                .setQuantity(quantity)
                .setLocked(isLocked)
                .setCategory(category)
                .setCreatedBy(getCreatedBy().toUserDTO());
    }

    public Product updateProduct(ProductDTO updates) {
        return this
                .setName(updates.getName())
                .setPrice(updates.getPrice())
                .setQuantity(updates.getQuantity())
                .setLocked(updates.isLocked())
                .setCategory(updates.getCategory());
    }
}
