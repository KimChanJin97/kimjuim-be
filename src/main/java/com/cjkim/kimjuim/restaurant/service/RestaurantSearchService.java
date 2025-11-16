package com.cjkim.kimjuim.restaurant.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import com.cjkim.kimjuim.mapper.RestaurantQueryMapper;
import com.cjkim.kimjuim.restaurant.domain.RestaurantDocument;
import com.cjkim.kimjuim.restaurant.dto.RestaurantAutocompleteResponse;
import com.cjkim.kimjuim.restaurant.dto.RestaurantNearbyResponse;
import com.cjkim.kimjuim.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private static final int MAX_RESULTS = 64;

    // 검색용 상수
    private static final float ADDRESS_BOOST = 4.0f;
    private static final float NAME_BOOST = 3.0f;
    private static final float CATEGORY_BOOST = 3.0f;
    private static final float MENU_BOOST = 2.0f;

    // 자동완성용 상수
    private static final int AUTOCOMPLETE_MAX_RESULTS = 3;
    private static final float AUTOCOMPLETE_MIN_SCORE = 3.0f;
    // 이름 필드 boost 값
    private static final float AUTOCOMPLETE_NAME_EXACT_BOOST = 15.0f;     // keyword 완전 일치
    private static final float AUTOCOMPLETE_NAME_MATCH_BOOST = 10.0f;     // 토큰 정확 매칭
    private static final float AUTOCOMPLETE_NAME_PREFIX_BOOST = 8.0f;     // 접두사 매칭
    private static final float AUTOCOMPLETE_NAME_NGRAM_BOOST = 5.0f;      // 부분 매칭 (ngram)
    // 주소 필드 boost 값
    private static final float AUTOCOMPLETE_ADDRESS_MATCH_BOOST = 3.0f;   // 일반 매칭
    private static final float AUTOCOMPLETE_ADDRESS_NGRAM_BOOST = 2.0f;   // ngram 매칭
    // 기타 필드 boost 값
    private static final float AUTOCOMPLETE_CATEGORY_BOOST = 4.0f;        // 카테고리
    private static final float AUTOCOMPLETE_MENU_BOOST = 3.0f;            // 메뉴

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchClient elasticsearchClient;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantQueryMapper restaurantQueryMapper;

    public List<RestaurantNearbyResponse> searchRestaurants(String keyword) {
        try {
            // 1. Nori 분석기로 토큰화
            List<String> tokens = analyzeWithNori(keyword);

            if (tokens.isEmpty()) {
                return List.of();
            }

            // 2. 각 토큰마다 모든 필드를 검색하는 쿼리 생성
            List<Query> shouldQueries = new ArrayList<>();

            for (String token : tokens) {
                // 각 토큰에 대해 dis_max: 모든 필드 중 최고 점수 1개만
                shouldQueries.add(Query.of(q -> q
                        .disMax(dm -> dm
                                .queries(
                                        // address
                                        Query.of(qq -> qq.constantScore(cs -> cs
                                                .filter(f -> f.match(m -> m
                                                        .field("address")
                                                        .query(token)
                                                ))
                                                .boost(ADDRESS_BOOST)
                                        )),

                                        // name
                                        Query.of(qq -> qq.constantScore(cs -> cs
                                                .filter(f -> f.match(m -> m
                                                        .field("name")
                                                        .query(token)
                                                ))
                                                .boost(NAME_BOOST)
                                        )),

                                        // category
                                        Query.of(qq -> qq.constantScore(cs -> cs
                                                .filter(f -> f.term(t -> t
                                                        .field("category")
                                                        .value(token)
                                                ))
                                                .boost(CATEGORY_BOOST)
                                        )),

                                        // menuNames
                                        Query.of(qq -> qq.constantScore(cs -> cs
                                                .filter(f -> f.match(m -> m
                                                        .field("menuNames")
                                                        .query(token)
                                                ))
                                                .boost(MENU_BOOST)
                                        ))
                                )
                        )
                ));


            }

            // 3. 최종 쿼리: 모든 토큰이 어느 하나의 필드에서 무조건 매칭되어야 함
            int minTokenMatch = tokens.size(); // 100%

            Query finalQuery = Query.of(q -> q.bool(b -> b
                    .should(shouldQueries)
                    .minimumShouldMatch(String.valueOf(minTokenMatch))
            ));

            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(finalQuery)
                    .withPageable(PageRequest.of(0, MAX_RESULTS))
                    .build();

            SearchHits<RestaurantDocument> searchHits = elasticsearchOperations.search(
                    searchQuery,
                    RestaurantDocument.class
            );

            List<Long> restaurantIds = searchHits.getSearchHits().stream()
                    .map(hit -> hit.getContent().getId())
                    .toList();

            if (restaurantIds.isEmpty()) {
                return List.of();
            }

            // PostgreSQL에서 실제 데이터 조회
            List<Map<String, Object>> queryResults = restaurantRepository.findRestaurantsByIds(restaurantIds);

            // RestaurantNearbyResponse로 변환
            return restaurantQueryMapper.mapToRestaurants(queryResults, LocalDateTime.now());

        } catch (Exception e) {
            log.error("Failed to search restaurants with keyword: {}", keyword, e);
            return List.of();
        }
    }

    public List<RestaurantAutocompleteResponse> autocompleteRestaurants(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return List.of();
            }

            // 1. Nori 분석기로 토큰화
            List<String> tokens = analyzeWithNori(keyword);

            if (tokens.isEmpty()) {
                return List.of();
            }

            // 2. 각 토큰마다 다층 매칭 쿼리 생성
            List<Query> shouldQueries = new ArrayList<>();

            for (String token : tokens) {
                // 이름 필드: 4단계 매칭 (정확 > 접두사 > 일반 > 부분)
                shouldQueries.add(Query.of(q -> q.bool(b -> b
                        .should(
                                // 1) 완전 일치 (최고 우선순위)
                                Query.of(qq -> qq.term(t -> t
                                        .field("name.keyword")
                                        .value(keyword.trim())
                                        .boost(AUTOCOMPLETE_NAME_EXACT_BOOST)
                                )),

                                // 2) 토큰 정확 매칭
                                Query.of(qq -> qq.match(m -> m
                                        .field("name")
                                        .query(token)
                                        .boost(AUTOCOMPLETE_NAME_MATCH_BOOST)
                                )),

                                // 3) 접두사 매칭
                                Query.of(qq -> qq.matchBoolPrefix(mbp -> mbp
                                        .field("name")
                                        .query(token)
                                        .boost(AUTOCOMPLETE_NAME_PREFIX_BOOST)
                                )),

                                // 4) ngram 부분 매칭
                                Query.of(qq -> qq.match(m -> m
                                        .field("name.ngram")
                                        .query(token)
                                        .boost(AUTOCOMPLETE_NAME_NGRAM_BOOST)
                                ))
                        )
                )));

                // 주소: ngram + 일반 매칭
                shouldQueries.add(Query.of(q -> q.bool(b -> b
                        .should(
                                Query.of(qq -> qq.match(m -> m
                                        .field("address")
                                        .query(token)
                                        .boost(AUTOCOMPLETE_ADDRESS_MATCH_BOOST)
                                )),
                                Query.of(qq -> qq.match(m -> m
                                        .field("address.ngram")
                                        .query(token)
                                        .boost(AUTOCOMPLETE_ADDRESS_NGRAM_BOOST)
                                ))
                        )
                )));

                // 카테고리: 정확 매칭만
                shouldQueries.add(Query.of(q -> q.term(t -> t
                        .field("category")
                        .value(token)
                        .boost(AUTOCOMPLETE_CATEGORY_BOOST)
                )));

                // 메뉴: 일반 매칭
                shouldQueries.add(Query.of(q -> q.match(m -> m
                        .field("menuNames")
                        .query(token)
                        .boost(AUTOCOMPLETE_MENU_BOOST)
                )));
            }

            // 3. 최종 쿼리: 유연한 매칭 (OR 방식)
            Query finalQuery = Query.of(q -> q.bool(b -> b
                    .should(shouldQueries)
                    .minimumShouldMatch("1")  // 최소 1개만 매칭되면 OK, 점수로 필터링
            ));

            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(finalQuery)
                    .withMinScore(AUTOCOMPLETE_MIN_SCORE)
                    .withPageable(PageRequest.of(0, AUTOCOMPLETE_MAX_RESULTS))
                    .build();

            SearchHits<RestaurantDocument> searchHits = elasticsearchOperations.search(
                    searchQuery,
                    RestaurantDocument.class
            );

            if (searchHits.isEmpty()) {
                return List.of();
            }

            List<RestaurantAutocompleteResponse> results = searchHits.getSearchHits().stream()
                    .map(hit -> RestaurantAutocompleteResponse.from(hit.getContent()))
                    .toList();

            return results;

        } catch (Exception e) {
            log.error("Failed to autocomplete restaurants with keyword: {}", keyword, e);
            return List.of();
        }
    }

    /**
     * Elasticsearch의 Nori 분석기로 텍스트를 토큰화
     */
    private List<String> analyzeWithNori(String text) {
        try {
            var response = elasticsearchClient.indices().analyze(a -> a
                    .index("restaurants")
                    .analyzer("nori_search_analyzer")
                    .text(text)
            );

            return response.tokens().stream()
                    .map(AnalyzeToken::token)
                    .filter(token -> token.length() > 1) // 1글자 토큰 제외
                    .distinct()
                    .toList();

        } catch (Exception e) {
            log.error("Failed to analyze text with Nori: {}", text, e);
            // Fallback: 공백 분리
            return Arrays.asList(text.trim().split("\\s+"));
        }
    }
}
