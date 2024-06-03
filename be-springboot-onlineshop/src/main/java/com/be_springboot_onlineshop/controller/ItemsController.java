package com.be_springboot_onlineshop.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.be_springboot_onlineshop.model.Customers;
import com.be_springboot_onlineshop.model.Items;
import com.be_springboot_onlineshop.service.ItemsService;

@RestController
@RequestMapping("/api/items")
public class ItemsController {
    @Autowired
    private ItemsService itemsService;

    @GetMapping
    public List<Items> getAllItemsWithAvailable() {
        return itemsService.getAllItemsWithAvailable();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Items> getItemByIdAndAvailable(@PathVariable UUID id) {
        Optional<Items> item = itemsService.getItemByIdAndAvailable(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Items> createItem(@RequestBody Items newItem) {
        Items item = itemsService.createItem(newItem);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Items> updateItemById(@PathVariable UUID id, @RequestBody Items updatedItem) {
        Items item = itemsService.updateItemById(id, updatedItem);
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemById(@PathVariable UUID id) {
        try {
            itemsService.deleteItemById(id);
            return ResponseEntity.ok("Data berhasil dihapus.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}