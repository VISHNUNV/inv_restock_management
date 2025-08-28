package com.inv_restock_management.controller;

import com.inv_restock_management.model.RestockRequest;
import com.inv_restock_management.model.RestockResponse;
import com.inv_restock_management.service.RestockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class RestockController {
    @Autowired
    RestockService stockService;

    @PostMapping("/restock")
    public RestockResponse checkForRestock(@RequestBody @Valid RestockRequest restockRequest) {
        return stockService.checkForRestock(restockRequest);
    }
}
