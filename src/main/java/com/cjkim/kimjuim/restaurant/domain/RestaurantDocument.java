package com.cjkim.kimjuim.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "restaurants")
public class RestaurantDocument {

    @Id
    private Long id;

    // name: 일반 검색 + 자동완성
    @MultiField(
            mainField = @Field(
                    type = FieldType.Text,
                    analyzer = "nori_analyzer",
                    searchAnalyzer = "nori_search_analyzer",
                    indexOptions = IndexOptions.docs,
                    norms = false
            ),
            otherFields = {
                    @InnerField(
                            suffix = "ngram",
                            type = FieldType.Text,
                            analyzer = "edge_ngram_analyzer",
                            searchAnalyzer = "nori_search_analyzer",
                            indexOptions = IndexOptions.docs,
                            norms = false
                    ),
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String name;

    // category: keyword 타입으로 정확 매칭만
    @Field(type = FieldType.Keyword)
    private String category;

    // address: 일반 검색 + 자동완성
    @MultiField(
            mainField = @Field(
                    type = FieldType.Text,
                    analyzer = "nori_analyzer",
                    searchAnalyzer = "nori_search_analyzer",
                    indexOptions = IndexOptions.docs,
                    norms = false
            ),
            otherFields = {
                    @InnerField(
                            suffix = "ngram",
                            type = FieldType.Text,
                            analyzer = "edge_ngram_analyzer",
                            searchAnalyzer = "nori_search_analyzer",
                            indexOptions = IndexOptions.docs,
                            norms = false
                    ),
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String address;

    // menuNames: 일반 검색 + 자동완성
    @MultiField(
            mainField = @Field(
                    type = FieldType.Text,
                    analyzer = "nori_analyzer",
                    searchAnalyzer = "nori_search_analyzer",
                    indexOptions = IndexOptions.docs,
                    norms = false
            ),
            otherFields = {
                    @InnerField(
                            suffix = "ngram",
                            type = FieldType.Text,
                            analyzer = "edge_ngram_analyzer",
                            searchAnalyzer = "nori_search_analyzer",
                            indexOptions = IndexOptions.docs,
                            norms = false
                    ),
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private List<String> menuNames;

    private String rid;
    private Double latitude;
    private Double longitude;
}