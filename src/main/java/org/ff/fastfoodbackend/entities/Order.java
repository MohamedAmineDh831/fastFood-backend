package org.ff.fastfoodbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ff.fastfoodbackend.entities.user.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }
}

enum OrderStatus {
    PENDING, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
}