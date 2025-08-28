package com.inv_restock_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inv_restock_management.model.Product;
import com.inv_restock_management.model.RestockRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestockControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Product product1;
    Product product2;
    Product product3;
    Product product4;
    List<Product> productList;

    @BeforeEach
    void init()
    {
        productList = new ArrayList<Product>();
        product1 = new Product(1L, "Laptop", 2, 5, true);
        product2 = new Product(2L, "Mouse", 10, 8, false);
        product3 = new Product(3L, "Keyboard", 6, 10, false);
        product4 = new Product(4L, "Charger", 7, 20, true);
    }

    @Test
    void testCheckForRestock() throws Exception {
        RestockRequest request = new RestockRequest();
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        request.setProducts(productList);

        mockMvc.perform(post("/inventory/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testArgumentInvalid() throws Exception {
        RestockRequest request = new RestockRequest();
        product1 = new Product(null, "Laptop", 2, 5, true);
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        request.setProducts(productList);

        mockMvc.perform(post("/inventory/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}