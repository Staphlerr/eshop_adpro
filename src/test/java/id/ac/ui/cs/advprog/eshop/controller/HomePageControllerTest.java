package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HomePageControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(new HomePageController()).build();
    }

    @Test
    void testHomePage() throws Exception {
        // Perform a GET request to the root URL ("/")
        mockMvc.perform(get("/"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(view().name("homePage")) // Expect the view name to be "homePage"
                .andExpect(model().size(0)); // Expect no model attributes
    }
}