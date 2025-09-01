package com.inv_restock_management.service;

import com.inv_restock_management.constants.Priority;
import com.inv_restock_management.model.Product;
import com.inv_restock_management.model.RestockRequest;
import com.inv_restock_management.model.RestockResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestockServiceTest {

    @Autowired
    RestockService service;

    Product product1;
    Product product2;
    Product product3;
    Product product4;
    List<Product> productList;

    @BeforeEach
    void init() {
        productList = new ArrayList<Product>();
        product1 = new Product(1L, "Laptop", 2, 5, true);
        product2 = new Product(2L, "Mouse", 10, 8, false);
        product3 = new Product(3L, "Keyboard", 6, 10, false);
        product4 = new Product(4L, "Charger", 7, 20, true);
    }

    @Test
    void checkForRestockValid() {
        RestockRequest request = new RestockRequest();
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        assertEquals(3, restockResponse.restockList().size());
        assertEquals(1, restockResponse.restockList().stream().filter(p -> p.name().equals("Laptop")).count());
        assertEquals(1, restockResponse.restockList().stream().filter(p -> p.name().equals("Keyboard")).count());
        assertEquals(1, restockResponse.restockList().stream().filter(p -> p.name().equals("Charger")).count());
    }

    @Test
    void checkForEmptyRestock() {
        RestockRequest request = new RestockRequest();
        productList.add(product2);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(0, restockResponse.restockList().size());
    }

    @Test
    void checkForCriticalItem() {
        product1 = new Product(1L, "Laptop", 0, 1, true);
        product2 = new Product(2L, "Mouse", 0, 1, true);

        RestockRequest request = new RestockRequest();
        productList.add(product1);
        productList.add(product2);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(2, restockResponse.restockList().size());
    }

    @Test
    void checkForNonCriticalItem() {
        product1 = new Product(1L, "Laptop", 8, 10, false);
        product2 = new Product(2L, "Mouse", 10, 20, false);
        product3 = new Product(3L, "Keyboard", 30, 20, false);

        RestockRequest request = new RestockRequest();
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(2, restockResponse.restockList().size());
    }

    @Test
    void checkForRestockQty() {
        product1 = new Product(1L, "Laptop", 5, 10, false);

        RestockRequest request = new RestockRequest();
        productList.add(product1);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(5, restockResponse.restockList().get(0).restockQuantity());
    }

    @Test
    void checkForZeroRestockQty() {
        product1 = new Product(1L, "Laptop", 10, 10, false);

        RestockRequest request = new RestockRequest();
        productList.add(product1);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(0, restockResponse.restockList().size());
    }

    @Test
    void checkForHighPriorityByStockPercentage() {
        product1 = new Product(1L, "Laptop", 3, 10, false);

        RestockRequest request = new RestockRequest();
        productList.add(product1);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(Priority.HIGH, restockResponse.restockList().get(0).priority());
    }

    @Test
    void checkForHighPriorityByCriticality() {
        product1 = new Product(1L, "Laptop", 9, 10, true);

        RestockRequest request = new RestockRequest();
        productList.add(product1);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(Priority.HIGH, restockResponse.restockList().get(0).priority());
    }

    @Test
    void checkForMediumPriority() {
        product1 = new Product(1L, "Laptop", 7, 10, false);

        RestockRequest request = new RestockRequest();
        productList.add(product1);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(Priority.MEDIUM, restockResponse.restockList().get(0).priority());
    }

    @Test
    void checkForLowPriority() {
        product1 = new Product(1L, "Mouse", 9, 10, false);

        RestockRequest request = new RestockRequest();
        productList.add(product1);
        request.setProducts(productList);

        RestockResponse restockResponse = service.checkForRestock(request);

        Assertions.assertEquals(Priority.LOW, restockResponse.restockList().get(0).priority());
    }

    @Test
    void checkForRequestNull() {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> service.checkForRestock(null));
        Assertions.assertEquals("Restock Request Should Not Be Null", nullPointerException.getMessage());
    }

    @Test
    void checkForRequestProductsNull() {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> service.checkForRestock(new RestockRequest()));
        Assertions.assertEquals("Restock Products Should Not Be Null", nullPointerException.getMessage());
    }
}