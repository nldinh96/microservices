package com.nldinh.inventory_service.controller;

import com.nldinh.inventory_service.dto.InventoryResponse;
import com.nldinh.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    //@PathVariable
    // http://localhost:8082/api/inventory/iphone-13,iphone13-red

    //@RequestParam
    //http://localhost:8082/api/inventory?skuCode=iphone13&skuCode=iphone13-red

    // -> @RequestParam is more readable

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInsStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInsStock(skuCode);
    }
}
