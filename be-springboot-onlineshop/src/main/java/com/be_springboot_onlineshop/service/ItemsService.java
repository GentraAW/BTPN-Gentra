package com.be_springboot_onlineshop.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be_springboot_onlineshop.model.Items;
import com.be_springboot_onlineshop.repository.ItemsRepo;
@Service
public class ItemsService {
    @Autowired
    private ItemsRepo itemsRepo;

    public List<Items> getAllItemsWithAvailable() {
        return itemsRepo.findByIsAvailable(true);
    }

    public Optional<Items> getItemByIdAndAvailable(UUID itemId) {
        return itemsRepo.findByItemIdAndIsAvailable(itemId, true);
    }

    public Items createItem(Items newItem){
        return itemsRepo.save(newItem);
    }

     public Items updateItemById(UUID itemId, Items updatedItem) {
        Optional<Items> itemOptional = itemsRepo.findById(itemId);
        if (itemOptional.isPresent()) {
            Items item = itemOptional.get();
            
            // update hanya field yang tidak null, jika null gunakan value seperti sebelumnya
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
    
    public void deleteItemById(UUID itemId) {
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
