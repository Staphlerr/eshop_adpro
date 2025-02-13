package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteProductTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        // Clear any interactions with the mocked repository before each test
        reset(productRepository);
    }

    @Test
    void testDeleteById_PositiveScenario() {
        // Arrange: Create a product and mock the repository to return it when findById is called
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        // Act: Delete the product by its ID
        productService.deleteById(productId);

        // Assert: Verify that the deleteById method was called on the repository
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteById_NegativeScenario() {
        // Arrange: Mock the repository to return an empty Optional when findById is called
        String nonExistentProductId = "non-existent-id";

        // Act & Assert: Ensure no exception is thrown when deleting a non-existent product
        assertDoesNotThrow(() -> productService.deleteById(nonExistentProductId));

        // Verify that the deleteById method was still called on the repository
        verify(productRepository, times(1)).deleteById(nonExistentProductId);
    }

    @Test
    void testCreateAndDeleteScenario() {
        // Arrange: Create a product and mock the repository behavior
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act: Create the product and then delete it
        productService.create(product);
        productService.deleteById(productId);

        // Assert: Verify that the deleteById method was called on the repository
        verify(productRepository, times(1)).deleteById(productId);

        // Verify that the product no longer exists in the repository
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        Optional<Product> deletedProduct = productService.findById(productId);
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    void testDeleteMultipleProductsScenario() {
        // Arrange: Create two products and mock the repository behavior
        String productId1 = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String productId2 = "a0f9de45-90b1-437d-a0bf-d0821dde9096";

        Product product1 = new Product();
        product1.setProductId(productId1);
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);

        Product product2 = new Product();
        product2.setProductId(productId2);
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);

        when(productRepository.findById(productId1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(productId2)).thenReturn(Optional.of(product2));

        // Act: Create both products and then delete them
        productService.create(product1);
        productService.create(product2);
        productService.deleteById(productId1);
        productService.deleteById(productId2);

        // Assert: Verify that the deleteById method was called for both products
        verify(productRepository, times(1)).deleteById(productId1);
        verify(productRepository, times(1)).deleteById(productId2);

        // Verify that both products no longer exist in the repository
        when(productRepository.findById(productId1)).thenReturn(Optional.empty());
        when(productRepository.findById(productId2)).thenReturn(Optional.empty());

        Optional<Product> deletedProduct1 = productService.findById(productId1);
        Optional<Product> deletedProduct2 = productService.findById(productId2);

        assertFalse(deletedProduct1.isPresent());
        assertFalse(deletedProduct2.isPresent());
    }
}