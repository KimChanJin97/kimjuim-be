//package com.cjkim.kimjuim.search;
//
//import com.cjkim.kimjuim.search.RestaurantSearchResponse;
//import com.cjkim.kimjuim.search.RestaurantSearchService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/search")
//@RequiredArgsConstructor
//public class RestaurantSearchController {
//
//    private final RestaurantSearchService restaurantSearchService;
//
//    @GetMapping("/restaurants")
//    public List<RestaurantSearchResponse> searchRestaurants(
//            @RequestParam String keyword
//    ) {
//        return restaurantSearchService.searchRestaurants(keyword);
//    }
//}