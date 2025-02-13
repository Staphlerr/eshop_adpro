package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}.
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void testCreateProductAndVerifyInList(ChromeDriver driver) throws Exception {
        // Step 1: Navigate to the home page and click the "Let's Create Product!" button
        driver.get(baseUrl);

        // Click the link to go to the product list page
        driver.findElement(By.linkText("Let's Create Product!")).click();

        // Verify navigation to the product list page
        String productListTitle = driver.getTitle();
        assertEquals("Product List", productListTitle);

        // Step 2: Click the "Create Product" button on the product list page
        driver.findElement(By.linkText("Create Product")).click();

        // Verify navigation to the create product page
        String createPageTitle = driver.getTitle();
        assertEquals("Create New Product", createPageTitle);

        // Step 3: Fill out the form and submit it
        String productName = "Sampo Cap Bambang";
        int productQuantity = 100;

        driver.findElement(By.id("nameInput")).sendKeys(productName);
        driver.findElement(By.id("quantityInput")).sendKeys(String.valueOf(productQuantity));
        driver.findElement(By.tagName("button")).click();

        // Step 4: Verify redirection back to the product list page
        String productListTitleAfterCreation = driver.getTitle();
        assertEquals("Product List", productListTitleAfterCreation);

        // Step 5: Verify that the new product is displayed in the product list
        String productListContent = driver.getPageSource();
        assertTrue(productListContent.contains(productName));
        assertTrue(productListContent.contains(String.valueOf(productQuantity)));

        // Optionally, verify the table row content
        String tableRowXPath = String.format("//td[text()='%s']/following-sibling::td[text()='%d']", productName, productQuantity);
        assertTrue(driver.findElements(By.xpath(tableRowXPath)).size() > 0, "The product should be listed in the table.");
    }
}