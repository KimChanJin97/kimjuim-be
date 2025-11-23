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

    // 검색용 상수
    private static final int SEARCH_MAX_RESULTS = 64;
    private static final float SEARCH_ADDRESS_BOOST = 4.0f;
    private static final float SEARCH_NAME_BOOST = 3.0f;
    private static final float SEARCH_CATEGORY_BOOST = 3.0f;
    private static final float SEARCH_MENU_BOOST = 2.0f;

    // 자동완성용 상수
    private static final int AUTOCOMPLETE_MAX_RESULTS = 3;
    private static final float AUTOCOMPLETE_MIN_SCORE = 10.0f;  // constantScore 적용 시 점수가 낮아짐
    private static final float AUTOCOMPLETE_NAME_EXACT_BOOST = 20.0f;
    private static final float AUTOCOMPLETE_NAME_MATCH_BOOST = 12.0f;
    private static final float AUTOCOMPLETE_NAME_PREFIX_BOOST = 6.0f;
    private static final float AUTOCOMPLETE_NAME_NGRAM_BOOST = 5.0f;

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchClient elasticsearchClient;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantQueryMapper restaurantQueryMapper;

    public List<RestaurantNearbyResponse> searchRestaurants(String keyword) {
        try {
            // 토큰화
            List<String> tokens = analyzeWithNori(keyword);

            if (tokens.isEmpty()) {
                return List.of();
            }

            List<Query> mustQueries = new ArrayList<>();

            // 모든 토큰 순회
            for (String token : tokens) {
                mustQueries.add(Query.of(q -> q
                        // disMax: 필드간 토큰 중복으로 인한 가중치 중복 방지
                        .disMax(dm -> dm
                                .queries(
                                        // address
                                        Query.of(qq -> qq.constantScore(cs -> cs
                                                .filter(f -> f.match(m -> m
                                                        .field("address")
                                                        .query(token)
                                                ))
                                                .boost(SEARCH_ADDRESS_BOOST)
                                        )),

                                        // name
                                        Query.of(qq -> qq.constantScore(cs -> cs
                                                .filter(f -> f.match(m -> m
                                                        .field("name")
                                                        .query(token)
                                                ))
                                                .boost(SEARCH_NAME_BOOST)
                                        )),

                                        // category
                                        Query.of(qq -> qq.constantScore(cs -> cs
                                                .filter(f -> f.term(t -> t
                                                        .field("category")
                                                        .value(token)
                                                ))
                                                .boost(SEARCH_CATEGORY_BOOST)
                                        )),

                                        // menuNames
                                        Query.of(qq -> qq.constantScore(cs -> cs
                                                .filter(f -> f.match(m -> m
                                                        .field("menuNames")
                                                        .query(token)
                                                ))
                                                .boost(SEARCH_MENU_BOOST)
                                        ))
                                )
                        )
                ));
            }

            // 모든 토큰이 반드시 매칭되어야 함 (disMax 유효성 검사?)
            Query finalQuery = Query.of(q -> q.bool(b -> b
                    .must(mustQueries)
            ));

            NativeQuery searchQuery = NativeQuery.builder()
                    .withQuery(finalQuery)
                    .withPageable(PageRequest.of(0, SEARCH_MAX_RESULTS))
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

            // 토큰화
            List<String> tokens = analyzeWithNori(keyword);

            if (tokens.isEmpty()) {
                return List.of();
            }

            List<Query> mustQueries = new ArrayList<>();

            // 모든 토큰 순회
            for (String token : tokens) {
                List<Query> tokenFieldQueries = new ArrayList<>();

                // name
                // 필드내 disMax: 필드내 토큰 중복으로 인한 가중치 중복 방지
                // + 검색어 완성/미완성 상태에 따른 가중치 계산 중 최고점
                tokenFieldQueries.add(Query.of(q -> q
                        .disMax(dm -> dm
                            .queries(
                                Query.of(qq -> qq.constantScore(cs -> cs
                                    .filter(f -> f.term(t -> t
                                        .field("name.keyword")
                                        .value(token)
                                    ))
                                    .boost(AUTOCOMPLETE_NAME_EXACT_BOOST)
                                )),
                                Query.of(qq -> qq.constantScore(cs -> cs
                                    .filter(f -> f.match(m -> m
                                        .field("name")
                                        .query(token)
                                    ))
                                    .boost(AUTOCOMPLETE_NAME_MATCH_BOOST)
                                )),
                                Query.of(qq -> qq.constantScore(cs -> cs
                                    .filter(f -> f.matchPhrasePrefix(mp -> mp
                                        .field("name")
                                        .query(token)
                                    ))
                                    .boost(AUTOCOMPLETE_NAME_PREFIX_BOOST)
                                )),
                                Query.of(qq -> qq.constantScore(cs -> cs
                                    .filter(f -> f.match(m -> m
                                        .field("name.ngram")
                                        .query(token)
                                    ))
                                    .boost(AUTOCOMPLETE_NAME_NGRAM_BOOST)
                                ))
                            )
                )));

                // address
                // 필드내 disMax: 필드내 토큰 중복으로 인한 가중치 중복 방지
                // + 검색어 완성/미완성 상태에 따른 가중치 계산 중 최고점
                tokenFieldQueries.add(Query.of(q -> q
                        .disMax(dm -> dm
                            .queries(
                                Query.of(qq -> qq.constantScore(cs -> cs
                                    .filter(f -> f.match(m -> m
                                        .field("address")
                                        .query(token)
                                    ))
                                    .boost(3.0f)
                                )),
                                Query.of(qq -> qq.constantScore(cs -> cs
                                    .filter(f -> f.match(m -> m
                                        .field("address.ngram")
                                        .query(token)
                                    ))
                                    .boost(2.0f)
                                ))
                            )
                )));

                // category
                tokenFieldQueries.add(Query.of(q -> q.constantScore(cs -> cs
                    .filter(f -> f.term(t -> t
                        .field("category")
                        .value(token)
                    ))
                    .boost(4.0f)
                )));

                // menu
                // 필드내 disMax: 필드내 토큰 중복으로 인한 가중치 중복 방지
                // + 검색어 완성/미완성 상태에 따른 가중치 계산 중 최고점
                tokenFieldQueries.add(Query.of(q -> q
                        .disMax(dm -> dm
                            .queries(
                                Query.of(qq -> qq.constantScore(cs -> cs
                                    .filter(f -> f.match(m -> m
                                        .field("menuNames")
                                        .query(token)
                                    ))
                                    .boost(3.0f)
                                )),
                                Query.of(qq -> qq.constantScore(cs -> cs
                                    .filter(f -> f.matchPhrasePrefix(mp -> mp
                                        .field("menuNames")
                                        .query(token)
                                    ))
                                    .boost(2.5f)
                                ))
                            )
                )));

                // 필드간 disMax: 필드간 토큰 중복으로 인한 가중치 중복 방지
                mustQueries.add(Query.of(q -> q.disMax(dm -> dm
                    .queries(tokenFieldQueries)
                )));
            }

            // 모든 토큰이 반드시 매칭되어야 함 (disMax 유효성 검사?)
            Query finalQuery = Query.of(q -> q.bool(b -> b
                .must(mustQueries)
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
                    .filter(token -> {
                        // 한글 1글자는 의미있는 검색어이므로 포함
                        if (token.length() == 1) {
                            return token.matches(".*[가-힣].*");
                        }
                        return token.length() > 1;
                    })
                    .distinct()
                    .toList();

        } catch (Exception e) {
            log.error("Failed to analyze text with Nori: {}", text, e);
            // Fallback: 공백 분리
            return Arrays.asList(text.trim().split("\\s+"));
        }
    }
}
