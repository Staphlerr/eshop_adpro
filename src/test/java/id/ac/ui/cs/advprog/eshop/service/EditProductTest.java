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
public class EditProductTest {

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
    void testUpdate_PositiveScenario() {
        // Arrange: Create an existing product and mock the repository to return it when findById is called
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Sampo Cap Bambang");
        existingProduct.setProductQuantity(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Act: Update the product with new details
        Product updatedProductDetails = new Product();
        updatedProductDetails.setProductId(productId);
        updatedProductDetails.setProductName("Sampo Cap Usep");
        updatedProductDetails.setProductQuantity(50);

        Product updatedProduct = productService.update(updatedProductDetails);

        // Assert: Verify that the product was updated correctly
        assertNotNull(updatedProduct);
        assertEquals(productId, updatedProduct.getProductId());
        assertEquals("Sampo Cap Usep", updatedProduct.getProductName());
        assertEquals(50, updatedProduct.getProductQuantity());

        // Verify that the repository's findById method was called
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testUpdate_NegativeScenario() {
        // Arrange: Mock the repository to return an empty Optional when findById is called
        String nonExistentProductId = "non-existent-id";

        when(productRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        // Act & Assert: Ensure that a RuntimeException is thrown when updating a non-existent product
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId(nonExistentProductId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.update(nonExistentProduct));
        assertEquals("Product not found with ID: " + nonExistentProductId, exception.getMessage());

        // Verify that the repository's findById method was called
        verify(productRepository, times(1)).findById(nonExistentProductId);
    }

    @Test
    void testCreateAndUpdateScenario() {
        // Arrange: Create a product and mock the repository behavior
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act: Create the product and then update it
        productService.create(product);

        Product updatedProductDetails = new Product();
        updatedProductDetails.setProductId(productId);
        updatedProductDetails.setProductName("Sampo Cap Usep");
        updatedProductDetails.setProductQuantity(50);

        Product updatedProduct = productService.update(updatedProductDetails);

        // Assert: Verify that the product was updated correctly
        assertNotNull(updatedProduct);
        assertEquals(productId, updatedProduct.getProductId());
        assertEquals("Sampo Cap Usep", updatedProduct.getProductName());
        assertEquals(50, updatedProduct.getProductQuantity());

        // Verify that the repository's findById method was called
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testUpdateAndDeleteScenario() {
        // Arrange: Create a product and mock the repository behavior
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act: Update the product and then delete it
        Product updatedProductDetails = new Product();
        updatedProductDetails.setProductId(productId);
        updatedProductDetails.setProductName("Sampo Cap Usep");
        updatedProductDetails.setProductQuantity(50);

        Product updatedProduct = productService.update(updatedProductDetails);
        productService.deleteById(productId);

        // Assert: Verify that the product no longer exists in the repository after deletion
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        Optional<Product> deletedProduct = productService.findById(productId);
        assertFalse(deletedProduct.isPresent());

        // Verify that the repository's findById and deleteById methods were called
        verify(productRepository, times(2)).findById(productId); // Once for update, once for delete
        verify(productRepository, times(1)).deleteById(productId);
    }
}