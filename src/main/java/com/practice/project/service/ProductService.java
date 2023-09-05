package com.practice.project.service;

import com.practice.project.modal.Category;
import com.practice.project.modal.Product;
import com.practice.project.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;
    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
    public void addProduct(Product product){
        productRepo.save(product);
    }
    public void removeProductById(Long id){
        productRepo.deleteById(id);
    }
    public Optional<Product> getProductById(Long id){
        return productRepo.findById(id);
    }
    public List<Product> getProductsByCategoryId(int id){
        return productRepo.findAllByCategory_Id(id);
    }
}
