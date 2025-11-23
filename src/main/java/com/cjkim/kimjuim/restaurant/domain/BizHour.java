package com.cjkim.kimjuim.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
public class BizHour {

    @Id
    private Long id;

    private String day;

    @Column(name = "biz_start")
    private String bizStart;

    @Column(name = "break_start")
    private String breakStart;

    @Column(name = "break_end")
    private String breakEnd;

    @Column(name = "last_order")
    private String lastOrder;

    @Column(name = "biz_end")
    private String bizEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
