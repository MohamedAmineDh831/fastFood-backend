package org.ff.fastfoodbackend.entities.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "restaurant_managers")
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class RestaurantManager extends User {

    // Plus tard, tu pourras faire une relation @OneToOne ou @ManyToOne vers le Restaurant
}