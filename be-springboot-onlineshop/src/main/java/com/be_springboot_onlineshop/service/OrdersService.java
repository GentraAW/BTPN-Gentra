
package com.be_springboot_onlineshop.service;
 
import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.be_springboot_onlineshop.model.Items;
import com.be_springboot_onlineshop.model.Orders;
import com.be_springboot_onlineshop.repository.ItemsRepo;
import com.be_springboot_onlineshop.repository.OrdersRepo;

@Service
 public class OrdersService {
     
    @Autowired
    private OrdersRepo ordersRepo;

    @Autowired
    private ItemsRepo itemsRepo;
    
    public List<Orders> getAllOrders() {       
        return ordersRepo.findAll();
    }
    
   public Optional<Orders> getOrderById(Long orderId) {
        return ordersRepo.findByOrderId(orderId);
    }

    public Orders createOrder(Orders newOrder) {
        // Mengambil data item berdasarkan item_id dari newOrder
        Items item = itemsRepo.findById(newOrder.getItems().getItemId()).orElse(null);

        // Jika item tidak ditemukan, kembalikan null atau lakukan penanganan error sesuai kebutuhan
        if (item == null) {
            throw new IllegalArgumentException("Item dengan ID " + newOrder.getItems().getItemId() + " tidak ditemukan");
        }

        // Menghitung total price berdasarkan price dari item dan quantity dari newOrder
        Integer totalPrice = item.getPrice() * newOrder.getQuantity();

        // Mengurangi stock item berdasarkan quantity dari newOrder
        Integer updatedStock = item.getStock() - newOrder.getQuantity();
        item.setStock(updatedStock);
        item.setLastReStock(new Date()); // Update lastReStock ke tanggal saat ini

        // Update total price dan stock item pada newOrder
        newOrder.setTotalPrice(totalPrice);
        newOrder.getItems().setStock(updatedStock); // Update stock pada item di newOrder

        // Simpan order dan item yang telah diupdate
        ordersRepo.save(newOrder);
        itemsRepo.save(item);

        return newOrder;
    }
    
    public Orders updateOrderById(Long orderId, Orders updatedOrder) {
        Optional<Orders> orderOptional = ordersRepo.findById(orderId);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();

            if (updatedOrder.getOrderCode() != null) {
                order.setOrderCode(updatedOrder.getOrderCode());
            }
            if (updatedOrder.getOrderDate() != null) {
                order.setOrderDate(updatedOrder.getOrderDate());
            }
            if (updatedOrder.getTotalPrice() != null) {
                order.setTotalPrice(updatedOrder.getTotalPrice());
            }
            if (updatedOrder.getCustomers() != null) {
                order.setCustomers(updatedOrder.getCustomers());
            }
            if (updatedOrder.getItems() != null) {
                order.setItems(updatedOrder.getItems());
            }
            if (updatedOrder.getQuantity() != null) {
                order.setQuantity(updatedOrder.getQuantity());
            }

            return ordersRepo.save(order);
        } else {
            return null;
        }
    }
    
    public void deleteOrderById(Long orderId) {
        if (ordersRepo.existsById(orderId)) {
            ordersRepo.deleteById(orderId);
        } else {
            throw new IllegalArgumentException("ID dengan nilai " + orderId + " tidak ditemukan");
        }
    }
}

