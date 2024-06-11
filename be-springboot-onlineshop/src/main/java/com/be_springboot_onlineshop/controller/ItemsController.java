package com.be_springboot_onlineshop.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.be_springboot_onlineshop.model.Items;
import com.be_springboot_onlineshop.service.ItemsService;

@RestController
@RequestMapping("/api/items")
public class ItemsController {
    @Autowired
    private ItemsService itemsService;

    @GetMapping
    public ResponseEntity<?> getAllAvailableItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "itemName") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(required = false) String itemName) {
        Page<Items> availableItems = itemsService.getAllAvailableItems(page, size, sortBy, direction, itemName);

        if (availableItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data sudah tidak tersedia");
        }

        return ResponseEntity.ok(availableItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Items> getItemByIdAndAvailable(@PathVariable Long id) {
        Optional<Items> item = itemsService.getItemByIdAndAvailable(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // @PostMapping
    // public ResponseEntity<Items> createItem(@RequestBody Items newItem) {
    //     Items item = itemsService.createItem(newItem);
    //     return ResponseEntity.ok(item);
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<Items> updateItemById(@PathVariable Long id, @RequestBody Items updatedItem) {
    //     Items item = itemsService.updateItemById(id, updatedItem);
    //     if (item != null) {
    //         return ResponseEntity.ok(item);
    //     } else {
    //         return ResponseEntity.notFound().build();
    //     }
    // }

    @PostMapping
    public Items createItem(
        @RequestParam String itemName,
        @RequestParam String itemCode,
        @RequestParam Integer stock,
        @RequestParam Integer price,
        @RequestParam(required = false) Boolean isAvailable
    ) {
        Items newItem = new Items();
        newItem.setItemName(itemName);
        newItem.setItemCode(itemCode);
        newItem.setStock(stock);
        newItem.setPrice(price);
        newItem.setIsAvailable(isAvailable != null ? isAvailable : true);
        newItem.setLastReStock(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));

        return itemsService.createItem(newItem);
    }

    @PutMapping("/{itemId}")
    public Items updateItemById(
        @PathVariable Long itemId,
        @RequestParam(required = false) String itemName,
        @RequestParam(required = false) String itemCode,
        @RequestParam(required = false) Integer stock,
        @RequestParam(required = false) Integer price,
        @RequestParam(required = false) Boolean isAvailable,
        @RequestParam(required = false) String lastReStock // Expected in ISO-8601 format
    ) {
        Items updatedItem = new Items();
        updatedItem.setItemName(itemName);
        updatedItem.setItemCode(itemCode);
        updatedItem.setStock(stock);
        updatedItem.setPrice(price);
        updatedItem.setIsAvailable(isAvailable);

        if (lastReStock != null) {
            updatedItem.setLastReStock(Date.from(Instant.parse(lastReStock)));
        }

        return itemsService.updateItemById(itemId, updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemById(@PathVariable Long id) {
        try {
            itemsService.deleteItemById(id);
            return ResponseEntity.ok("Data berhasil dihapus.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}