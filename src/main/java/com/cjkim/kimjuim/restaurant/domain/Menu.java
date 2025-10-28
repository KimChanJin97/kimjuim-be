package com.cjkim.kimjuim.restaurant.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
public class Menu {

    @Id
    private Long id;

    private String name;

    private String price;

    private boolean isRecommended;

    private String description;

    @Column(name = "menu_idx")
    private int menuIdx;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menu")
    private Set<MenuImage> menuImages;
}
