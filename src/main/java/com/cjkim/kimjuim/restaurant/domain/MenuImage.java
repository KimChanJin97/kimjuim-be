package com.cjkim.kimjuim.restaurant.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
public class MenuImage {

    @Id
    private Long id;

    @Column(name = "image_url", length = 2000)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "menu_id") // FK
    private Menu menu;
}
