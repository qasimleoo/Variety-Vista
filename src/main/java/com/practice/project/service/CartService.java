package com.practice.project.service;

import com.practice.project.modal.Product;
import com.practice.project.modal.User;
import com.practice.project.modal.UserCart;
import com.practice.project.repository.UserCartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    UserCartRepo userCartRepository;
    @Autowired
    ProductService productService;

    public UserCart addToCart(User user, Product product) {
        UserCart cartItem = new UserCart();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        user.getCartItems().add(cartItem);
        userCartRepository.save(cartItem);
        return cartItem;
    }

    public void removeById(Long id){
        userCartRepository.deleteById(id);
    }
    public Long getUserCartIdByProductId(Long productId, User user) {
        UserCart userCart = userCartRepository.findByProductAndUser(productService.getProductById(productId).get(), user);
        return userCart != null ? userCart.getId() : null;
    }

    public List<UserCart> getUserCartItems(User user) {
        return userCartRepository.findByUser(user);
    }
}