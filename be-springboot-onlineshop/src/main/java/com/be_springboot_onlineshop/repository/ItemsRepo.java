package com.be_springboot_onlineshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.be_springboot_onlineshop.model.Items;

@Repository
public interface ItemsRepo extends JpaRepository<Items, Long> {
    Page<Items> findByIsAvailable(boolean isAvailable, Pageable pageable);

    Optional<Items> findByItemIdAndIsAvailable(Long itemId, boolean isAvailable);
    Page<Items> findByItemNameContainingIgnoreCaseAndIsAvailable(String itemName, boolean isAvailable, Pageable pageable);

}
