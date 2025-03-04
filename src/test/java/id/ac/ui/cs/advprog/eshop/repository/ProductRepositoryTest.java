package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Clear the repository before each test to ensure a clean state
        productRepository = new ProductRepository();
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();
        assertEquals(savedProduct.getProductId(), product.getProductId());
        assertEquals(savedProduct.getProductName(), product.getProductName());
        assertEquals(savedProduct.getProductQuantity(), product.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de45-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());

        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());

        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindById_PositiveScenario() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Optional<Product> foundProduct = productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        assertTrue(foundProduct.isPresent());
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", foundProduct.get().getProductId());
        assertEquals("Sampo Cap Bambang", foundProduct.get().getProductName());
        assertEquals(100, foundProduct.get().getProductQuantity());
    }

    @Test
    void testFindById_NegativeScenario() {
        Optional<Product> foundProduct = productRepository.findById("non-existent-id");
        assertFalse(foundProduct.isPresent());
    }

    @Test
    void testDeleteById_PositiveScenario() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.deleteById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        Optional<Product> foundProduct = productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        assertFalse(foundProduct.isPresent());
    }

    @Test
    void testDeleteById_NegativeScenario() {
        productRepository.deleteById("non-existent-id");

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testCreate_GenerateProductId() {
        Product product = new Product();
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        Product savedProduct = productRepository.create(product);

        assertNotNull(savedProduct.getProductId());
        assertFalse(savedProduct.getProductId().isEmpty());

        assertDoesNotThrow(() -> UUID.fromString(savedProduct.getProductId()));

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getProductId());
        assertTrue(foundProduct.isPresent());
        assertEquals("Sampo Cap Bambang", foundProduct.get().getProductName());
        assertEquals(100, foundProduct.get().getProductQuantity());
    }

    @Test
    void testCreate_GenerateProductId_WhenNull() {
        Product product = new Product();
        product.setProductId(null);
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        Product savedProduct = productRepository.create(product);

        assertNotNull(savedProduct.getProductId());
        assertFalse(savedProduct.getProductId().isEmpty());

        assertDoesNotThrow(() -> UUID.fromString(savedProduct.getProductId()));

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getProductId());
        assertTrue(foundProduct.isPresent());
        assertEquals("Sampo Cap Bambang", foundProduct.get().getProductName());
        assertEquals(100, foundProduct.get().getProductQuantity());
    }

    @Test
    void testCreate_GenerateProductId_WhenEmpty() {
        Product product = new Product();
        product.setProductId("");
        product.setProductName("Sampo Cap Usep");
        product.setProductQuantity(50);

        Product savedProduct = productRepository.create(product);

        assertNotNull(savedProduct.getProductId());
        assertFalse(savedProduct.getProductId().isEmpty());

        assertDoesNotThrow(() -> UUID.fromString(savedProduct.getProductId()));

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getProductId());
        assertTrue(foundProduct.isPresent());
        assertEquals("Sampo Cap Usep", foundProduct.get().getProductName());
        assertEquals(50, foundProduct.get().getProductQuantity());
    }
}