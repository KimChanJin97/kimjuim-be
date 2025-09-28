package com.cjkim.kimjuim.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
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
@EqualsAndHashCode(of = {"title"}, callSuper = false)
@Entity
public class Review {

    @Id
    private Long id;

    private String name;

    private String type;

    @Column(length = 2000)
    private String url;

    private String title;

    @Column(name = "review_idx")
    private String reviewIdx;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "profile_url", length = 2000)
    private String profileUrl;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "created_at")
    private String createdAt;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImages;
}
