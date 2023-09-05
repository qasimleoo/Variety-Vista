package com.practice.project.repository;

import com.practice.project.modal.Product;
import com.practice.project.modal.User;
import com.practice.project.modal.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCartRepo extends JpaRepository<UserCart, Long> {
    UserCart findByProductAndUser(Product product, User user);
    List<UserCart> findByUser(User user);
}