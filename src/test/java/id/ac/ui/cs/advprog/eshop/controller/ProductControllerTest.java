package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    private Product mockProduct;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        // Create a mock product for testing
        mockProduct = new Product();
        mockProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        mockProduct.setProductName("Sampo Cap Bambang");
        mockProduct.setProductQuantity(100);
    }

    @Test
    void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testCreateProductPost() throws Exception {
        mockMvc.perform(post("/product/create")
                        .flashAttr("product", mockProduct))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(productService, times(1)).create(mockProduct);
    }

    @Test
    void testProductListPage() throws Exception {
        when(productService.findAll()).thenReturn(Collections.singletonList(mockProduct));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attribute("products", Collections.singletonList(mockProduct)));

        verify(productService, times(1)).findAll();
    }

    @Test
    void testEditProductPage_ProductFound() throws Exception {
        when(productService.findById("eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/product/edit/eb558e9f-1c39-460e-8860-71af6af63bd6"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("product", mockProduct));

        verify(productService, times(1)).findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
    }

    @Test
    void testEditProductPage_NonExistingProduct() {
        // Arrange: Mock the service to return an empty Optional when findById is called
        String productId = "nonExistingId";
        when(productService.findById(productId)).thenReturn(Optional.empty());

        // Act: Call the editProductPage method directly
        String viewName = productController.editProductPage(productId, model);

        // Assert: Verify that no product was added to the model
        verify(model, never()).addAttribute(eq("product"), any());

        // Assert: Verify that the view name is a redirect to the product list page
        assertEquals("redirect:/product/list", viewName);

        // Verify that the findById method was called with the correct ID
        verify(productService, times(1)).findById(productId);
    }

    @Test
    void testEditProductPost() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .flashAttr("product", mockProduct))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(productService, times(1)).update(mockProduct);
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(get("/product/delete/eb558e9f-1c39-460e-8860-71af6af63bd6"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("../list"));

        verify(productService, times(1)).deleteById("eb558e9f-1c39-460e-8860-71af6af63bd6");
    }
}