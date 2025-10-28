//package com.cjkim.kimjuim.search;
//
//import java.util.List;
//
//public record RestaurantSearchResponse(
//        Long id,
//        String rid,
//        String name,
//        String category,
//        String address,
//        Double latitude,
//        Double longitude,
//        List<String>menuNames,
//        Float score
//) {
//    public static RestaurantSearchResponse from(RestaurantDocument document, float score) {
//        return new RestaurantSearchResponse(
//                document.getId(),
//                document.getRid(),
//                document.getName(),
//                document.getCategory(),
//                document.getAddress(),
//                document.getLocation().getLat(),
//                document.getLocation().getLon(),
//                document.getMenuNames(),
//                score
//        );
//    }
//}
