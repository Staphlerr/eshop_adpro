package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product create(Product product);
    List<Product> findAll();
    Optional<Product> findById(String productId); // Add this method
    Product update(Product product);
    void deleteById(String productId);
}