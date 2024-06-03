package com.be_springboot_onlineshop.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.be_springboot_onlineshop.model.Items;

@Repository
public interface ItemsRepo extends JpaRepository<Items, UUID>{
    List<Items> findByIsAvailable(boolean isAvailable);
    Optional<Items> findByItemIdAndIsAvailable(UUID itemId, boolean isAvailable);
}
