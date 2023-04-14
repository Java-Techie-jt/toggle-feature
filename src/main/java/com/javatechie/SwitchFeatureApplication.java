package com.javatechie;

import com.fasterxml.jackson.databind.util.Named;
import com.javatechie.dto.Product;
import com.javatechie.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.util.NamedFeature;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class SwitchFeatureApplication {
    @Autowired
    private FeatureManager manager;

    public static final Feature DISCOUNT_APPLIED = new NamedFeature("DISCOUNT_APPLIED");

    @Autowired
    private InventoryService service;


    @GetMapping("/orders")
    public List<Product> showAvailableProducts() {
        if (manager.isActive(DISCOUNT_APPLIED)) {
            return applyDiscount(service.getAllProducts());
        } else {
            return service.getAllProducts();
        }
    }

    private List<Product> applyDiscount(List<Product> availableProducts) {
        List<Product> orderListAfterDiscount = new ArrayList<>();
        service.getAllProducts().forEach(order -> {
            order.setPrice(order.getPrice() - (order.getPrice() * 5 / 100));
            orderListAfterDiscount.add(order);
        });
        return orderListAfterDiscount;
    }


    public static void main(String[] args) {
        SpringApplication.run(SwitchFeatureApplication.class, args);
    }


}
