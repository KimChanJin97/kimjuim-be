//package com.cjkim.kimjuim.search;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//import org.springframework.data.elasticsearch.annotations.GeoPointField;
//import org.springframework.data.elasticsearch.core.geo.GeoPoint;
//
//import java.util.List;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Document(indexName = "restaurants")
//public class RestaurantDocument {
//
//    @Id
//    private Long id;
//
//    @Field(type = FieldType.Text, analyzer = "nori")
//    private String name;
//
//    @Field(type = FieldType.Keyword)
//    private String category;
//
//    @Field(type = FieldType.Text, analyzer = "nori")
//    private String address;
//
//    @GeoPointField
//    private GeoPoint location;
//
//    @Field(type = FieldType.Text, analyzer = "nori")
//    private List<String> menuNames;
//
//    @Field(type = FieldType.Keyword)
//    private String rid;
//}