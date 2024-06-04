package com.be_springboot_onlineshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.be_springboot_onlineshop.model.Items;

@Repository
public interface ItemsRepo extends JpaRepository<Items, Long> {
    List<Items> findByIsAvailable(boolean isAvailable);

    Optional<Items> findByItemIdAndIsAvailable(Long itemId, boolean isAvailable);
}
