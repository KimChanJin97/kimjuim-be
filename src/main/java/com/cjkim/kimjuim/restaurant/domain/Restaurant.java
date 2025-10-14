package com.cjkim.kimjuim.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table(indexes = {@Index(name = "idx_coordinate", columnList = "coordinate")})
@Entity
public class Restaurant {

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

    @Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "restaurant")
    List<RestaurantImage> restaurantImages = new ArrayList<>();

    @Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "restaurant")
    List<Menu> menus = new ArrayList<>();

    @Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "restaurant")
    List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "restaurant")
    List<BizHour> bizHours = new ArrayList<>();
}

