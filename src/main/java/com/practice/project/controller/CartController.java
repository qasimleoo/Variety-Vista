package com.practice.project.controller;

import com.practice.project.global.GlobalData;
import com.practice.project.modal.Product;
import com.practice.project.modal.User;
import com.practice.project.modal.UserCart;
import com.practice.project.repository.UserRepo;
import com.practice.project.service.CartService;
import com.practice.project.service.CustomUserDetailService;
import com.practice.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController  {
    @Autowired
    ProductService productService;
    @Autowired
    CustomUserDetailService customUserDetailService;
    @Autowired
    CartService cartService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id, Model model, Principal principal) {
        GlobalData.cart.add(productService.getProductById(id).get());
        model.addAttribute("productId", id);
        User user = (User) customUserDetailService.loadUserByUsername(principal.getName());
        Product product = productService.getProductById(id).get();
        UserCart userCart = cartService.addToCart(user, product);
        return "addedToCart";
    }

    @GetMapping("/cart/removeItem/{index}")
    public String removeItem(@PathVariable int index, Principal principal) {
        if (index >= 0 && index < GlobalData.cart.size()) {
            User user = (User) customUserDetailService.loadUserByUsername(principal.getName());
            Long userCartId = cartService.getUserCartIdByProductId(GlobalData.cart.get(index).getId(), user);
            if (userCartId != null) {
                cartService.removeById(userCartId);
            }
            System.out.println(userCartId);
            GlobalData.cart.remove(index);
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cartGet(Model model, Principal principal){
        User user = (User) customUserDetailService.loadUserByUsername(principal.getName());
        List<UserCart> cartItems = cartService.getUserCartItems(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("cart", GlobalData.cart);
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model){
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        return "checkout";
    }
}
