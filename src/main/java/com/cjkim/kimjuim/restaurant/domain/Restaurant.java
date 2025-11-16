package com.cjkim.kimjuim.restaurant.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table(indexes = {@Index(name = "idx_coordinate", columnList = "coordinate")})
@Entity
public class Restaurant {

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    Set<RestaurantImage> restaurantImages = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    Set<Menu> menus = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    Set<Review> reviews = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    Set<BizHour> bizHours = new HashSet<>();

    @Id
    private Long id;
    private String rid;
    private String name;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point coordinate;

    private String category;
    private String address;

    @Column(name = "road_address")
    private String roadAddress;
}

