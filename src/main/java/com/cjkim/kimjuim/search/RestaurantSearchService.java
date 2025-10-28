//package com.cjkim.kimjuim.search;
//
//import co.elastic.clients.elasticsearch._types.query_dsl.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.elasticsearch.client.elc.NativeQuery;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class RestaurantSearchService {
//
//    private final ElasticsearchOperations elasticsearchOperations;
//
//    public List<RestaurantSearchResponse> searchRestaurants(String keyword) {
//        // Multi-match 쿼리 (name, category, address, menuNames에서 검색)
//        Query multiMatchQuery = MultiMatchQuery.of(m -> m
//                .fields("name^3", "category^2", "menuNames^2", "address^1")  // 가중치 부여
//                .query(keyword)
//                .analyzer("nori")
//                .fuzziness("AUTO")
//        )._toQuery();
//
//        // Function Score로 추가 점수 조정
//        Query functionScoreQuery = FunctionScoreQuery.of(f -> f
//                .query(multiMatchQuery)
//                .functions(fn -> fn
//                        .filter(Query.of(q -> q
//                                .match(MatchQuery.of(m -> m
//                                                .field("name")
//                                                .query(keyword)
//                                        )
//                                )))
//                        .weight(2.0)  // name 정확히 매칭 시 가중치 2배
//                )
//                .boostMode(FunctionBoostMode.Multiply)
//                .scoreMode(FunctionScoreMode.Sum)
//        )._toQuery();
//
//        // Native Query 생성
//        NativeQuery searchQuery = NativeQuery.builder()
//                .withQuery(functionScoreQuery)
//                .withMaxResults(10)
//                .build();
//
//        // 검색 실행
//        SearchHits<RestaurantDocument> searchHits =
//                elasticsearchOperations.search(searchQuery, RestaurantDocument.class);
//
//        // 결과 변환 (점수 내림차순)
//        return searchHits.getSearchHits().stream()
//                .map(hit -> RestaurantSearchResponse.from(hit.getContent(), hit.getScore()))
//                .collect(Collectors.toList());
//    }
//}