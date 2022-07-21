package com.codegym.controller.api;

import com.codegym.exception.DataInputException;
import com.codegym.exception.ProductLockedException;
import com.codegym.exception.UserBlockedException;
import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.Role;
import com.codegym.model.User;
import com.codegym.model.dto.ProductDTO;
import com.codegym.model.dto.UserDTO;
import com.codegym.security.SecurityAuditorAware;
import com.codegym.service.product.ICategoryService;
import com.codegym.service.product.IProductService;
import com.codegym.service.role.IRoleService;
import com.codegym.service.user.IUserService;
import com.codegym.utility.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IUserService userService;

    @Autowired
    private SecurityAuditorAware securityAuditorAware;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/products")
    public ResponseEntity<?> showProductList() {
        Long currentUserId = securityAuditorAware.getCurrentAuditor().get().getId();
        List<ProductDTO> productList = new ArrayList<>();
        if (roleService.checkIfADMIN(currentUserId)) {
            productList = productService.findAllDTO();
        } else {
            productList = productService.findAllOwnedProductsDTO(currentUserId);
        }

        if (productList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> selectProduct(@PathVariable("id") String rawId) {
        try {
            Long id = Long.parseLong(rawId);
            if (!productService.ifExists(id))
                throw new NoSuchElementException();

            Long currentUserId = securityAuditorAware.getCurrentAuditor().get().getId();
            if (!productService.checkIfProductIsRightfullyOwned(id, currentUserId))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            ProductDTO productDTO = productService.findDTOById(id);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);

        } catch (NumberFormatException | NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        List<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/currentUsername")
    public ResponseEntity<?> getCurrentUsername() {
        String username = securityAuditorAware.getCurrentAuditor().get().getUsername();
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @PostMapping("/product/create")
    public ResponseEntity<?> createProduct(@Validated @RequestBody ProductDTO productDTO,
                                           BindingResult bindingResult) {
        Category category = productDTO.getCategory();

        if (!categoryService.ifExists(category.getId()))
            bindingResult.addError(new FieldError("category", "category", "Category invalid"));

        category = categoryService.findById(category.getId());

        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        try {
            Product newProduct = productService.create(productDTO);
            productDTO = newProduct.toProductDTO();
            productDTO.setCategory(category);
            return new ResponseEntity<>(productDTO, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Account information is not valid, please check the information again");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/product/edit/{id}")
    public ResponseEntity<?> editProduct(@PathVariable("id") String rawId,
                                         @Validated @RequestBody ProductDTO productDTO,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        Long id;
        Category category = productDTO.getCategory();

        try {
            id = Long.parseLong(rawId);
            if (!productService.ifExists(id))
                throw new IOException();
            if (productService.ifLocked(id))
                throw new ProductLockedException("This product has already been locked");
        } catch (NumberFormatException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productDTO.setId(id);

        if (!categoryService.ifExists(category.getId()))
            bindingResult.addError(new FieldError("category", "category", "Category invalid"));

        category = categoryService.findById(category.getId());

        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        try {
            Product updatedProduct = productService.update(productDTO);
            productDTO = updatedProduct.toProductDTO();
            productDTO.setCategory(category);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/product/lock/{id}")
    public ResponseEntity<?> lockProduct(@PathVariable("id") String rawId) {
        Long id;

        try {
            id = Long.parseLong(rawId);
            if (!productService.ifExists(id))
                throw new IOException();
            if (productService.ifLocked(id))
                throw new ProductLockedException("This product has already been locked");

            Product product = productService.lock(id);
            ProductDTO productDTO = product.toProductDTO();
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (NumberFormatException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/product/unlock/{id}")
    public ResponseEntity<?> unlockProduct(@PathVariable("id") String rawId) {
        Long id;

        try {
            id = Long.parseLong(rawId);
            if (!productService.ifExists(id))
                throw new IOException();
            if (!productService.ifLocked(id))
                throw new ProductLockedException("This product has not been locked");

            Product product = productService.unlock(id);
            ProductDTO productDTO = product.toProductDTO();
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (NumberFormatException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> showUserList() {
        List<UserDTO> userList = userService.findAllDTO();

        if (userList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> selectUser(@PathVariable("id") String rawId) {
        try {
            Long id = Long.parseLong(rawId);
            if (!userService.ifExists(id))
                throw new NoSuchElementException();
            UserDTO userDTO = userService.findDTOById(id);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NumberFormatException | NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserDTO userDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        Optional<UserDTO> optUser = userService.findUserDTOByUsername(userDTO.getUsername());
        if (optUser.isPresent())
            bindingResult.addError(new FieldError("username", "username", "Username already exists"));

        Optional<Role> optRole = roleService.findOptById(userDTO.getRole().getId());
        if (!optRole.isPresent())
            bindingResult.addError(new FieldError("role", "role", "Invalid role"));

        if (userService.ifEmailExists(userDTO.getEmail()))
            bindingResult.addError(new FieldError("email", "email", "This email has already been registered"));

        if (userService.ifPhoneExists(userDTO.getPhone()))
            bindingResult.addError(new FieldError("phone", "phone", "This phone has already been registered"));

        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        try {
            User newUser = userService.create(userDTO);
            newUser.setRole(optRole.get());
            userDTO = newUser.toUserDTO();
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Account information is not valid, please check the information again");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/user/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> editUser(@PathVariable("id") String rawId,
                                      @Validated @RequestBody UserDTO userDTO,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        Long id;

        try {
            id = Long.parseLong(rawId);
            if (!userService.ifExists(id))
                throw new IOException();
            if (userService.ifBlocked(id))
                throw new UserBlockedException("This user has already been blocked");
        } catch (NumberFormatException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userDTO.setId(id);

        if (userService.ifEmailExistsExceptSelf(userDTO.getEmail(), id))
            bindingResult.addError(new FieldError("email", "email", "This email has already been registered"));
        if (userService.ifPhoneExistsExceptSelf(userDTO.getPhone(), id))
            bindingResult.addError(new FieldError("phone", "phone", "This number has already been registered"));

        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        try {
            User updatedUser = userService.update(userDTO);
            userDTO = updatedUser.toUserDTO();
            Role role = roleService.findById(userDTO.getRole().getId());
            userDTO.setRole(role.toRoleDTO());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/user/block/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> blockUser(@PathVariable("id") String rawId) {
        Long id;

        try {
            id = Long.parseLong(rawId);
            if (!userService.ifExists(id))
                throw new IOException();
            if (userService.ifBlocked(id))
                throw new UserBlockedException("This user has already been blocked");

            User user = userService.block(id);
            UserDTO userDTO = user.toUserDTO();
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NumberFormatException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/user/unblock/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> unblockUser(@PathVariable("id") String rawId) {
        Long id;

        try {
            id = Long.parseLong(rawId);
            if (!userService.ifExists(id))
                throw new IOException();
            if (!userService.ifBlocked(id))
                throw new UserBlockedException("This user have not been blocked");

            User user = userService.unblock(id);
            UserDTO userDTO = user.toUserDTO();
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NumberFormatException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
