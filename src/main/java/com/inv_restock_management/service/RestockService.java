package com.inv_restock_management.service;

import com.inv_restock_management.constants.Priority;
import com.inv_restock_management.model.Product;
import com.inv_restock_management.model.Restock;
import com.inv_restock_management.model.RestockRequest;
import com.inv_restock_management.model.RestockResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RestockService {
    public RestockResponse checkForRestock(RestockRequest restockRequest) {
        List<Product> restockProducts = getRestockRequiredProducts(restockRequest.getProducts());
        List<Restock> restocks = prepareRestockResponse(restockProducts);

        return new RestockResponse(restocks);
    }

    private List<Product> getRestockRequiredProducts(List<Product> reqProducts) {
        return reqProducts.stream().filter(p -> (p.currentStock() < p.minRequiredStock())
                || (p.criticalItem() && p.currentStock() < 2)).toList();
    }

    private List<Restock> prepareRestockResponse(List<Product> restockProducts) {
        List<Restock> restocks = new ArrayList<>();

        for (Product product : restockProducts) {
            Integer restockQty = getRestockQty(product.minRequiredStock(), product.currentStock());
            if (restockQty > 0) {
                Restock restock = new Restock(
                        product.id(),
                        product.name(),
                        restockQty,
                        getPriority(product));
                restocks.add(restock);
            }
        }

        return restocks.stream()
                .sorted(Comparator.comparing(Restock::priority, Comparator.comparingInt(Priority::getOrder))
                        .thenComparing(Restock::name))
                .toList();
    }

    private Integer getRestockQty(Integer minReqStock, Integer currentStock) {
        return minReqStock - currentStock;
    }

    private Priority getPriority(Product product) {

        Integer stockPercentage = (int) Math.round(100.0 * product.currentStock() / product.minRequiredStock());
        if (product.criticalItem() || stockPercentage < 50) {
            return Priority.HIGH;
        } else if (stockPercentage >= 50 && stockPercentage <= 80) {
            return Priority.MEDIUM;
        } else {
            return Priority.LOW;
        }
    }
}
