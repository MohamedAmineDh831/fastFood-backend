package org.ff.fastfoodbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ff.fastfoodbackend.entities.user.DeliveryPerson;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPerson deliveryPerson;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @PrePersist
    protected void onAssign() {
        this.assignedAt = LocalDateTime.now();
        this.status = DeliveryStatus.ASSIGNED;
    }
}

enum DeliveryStatus {
    ASSIGNED,
    PICKED_UP,
    ON_THE_WAY,
    DELIVERED,
    FAILED
}