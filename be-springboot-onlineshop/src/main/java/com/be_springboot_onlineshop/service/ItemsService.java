package com.be_springboot_onlineshop.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.be_springboot_onlineshop.model.Items;
import com.be_springboot_onlineshop.repository.ItemsRepo;

@Service
public class ItemsService {
    @Autowired
    private ItemsRepo itemsRepo;

    public Page<Items> getAllAvailableItems(int page, int size, String sortBy, String direction, String itemName) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Items> availableItemsPage;
        if (itemName == null || itemName.isEmpty()) {
            availableItemsPage = itemsRepo.findByIsAvailable(true, pageable);
        } else {
            availableItemsPage = itemsRepo.findByItemNameContainingIgnoreCaseAndIsAvailable(itemName, true, pageable);
        }
        
        return availableItemsPage;
    }

    public Optional<Items> getItemByIdAndAvailable(Long itemId) {
        return itemsRepo.findByItemIdAndIsAvailable(itemId, true);
    }

    public Items createItem(Items newItem) {
        newItem.setIsAvailable(true);
        newItem.setLastReStock(new Date());

        return itemsRepo.save(newItem);
    }

    public Items updateItemById(Long itemId, Items updatedItem) {
        
        Optional<Items> itemOptional = itemsRepo.findById(itemId);
        if (itemOptional.isPresent()) {
            Items item = itemOptional.get();

            if (updatedItem.getItemName() != null) {
                item.setItemName(updatedItem.getItemName());
            }
            if (updatedItem.getItemCode() != null) {
                item.setItemCode(updatedItem.getItemCode());
            }
            if (updatedItem.getStock() != null) {
                item.setStock(updatedItem.getStock());
            }
            if (updatedItem.getPrice() != null) {
                item.setPrice(updatedItem.getPrice());
            }
            if (updatedItem.getIsAvailable() != null) {
                item.setIsAvailable(updatedItem.getIsAvailable());
            }
            if (updatedItem.getLastReStock() != null) {
                item.setLastReStock(updatedItem.getLastReStock());
            }

            return itemsRepo.save(item);
        } else {
            return null;
        }
    }

    public void deleteItemById(Long itemId) {
        Optional<Items> itemOptional = itemsRepo.findById(itemId);
        if (itemOptional.isPresent()) {
            Items item = itemOptional.get();
            if (!item.getIsAvailable()) {
                throw new IllegalArgumentException("Data dengan ID tersebut sudah dihapus.");
            }
            item.setIsAvailable(false);
            itemsRepo.save(item);
        } else {
            throw new IllegalArgumentException("ID tidak di temukan.");
        }
    }
}
