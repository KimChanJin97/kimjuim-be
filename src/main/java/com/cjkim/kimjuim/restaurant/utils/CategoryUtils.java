package com.cjkim.kimjuim.restaurant.utils;

import java.util.List;

public class CategoryUtils {

    private static final List<String> HANSIK = List.of("한식", "생선", "장어", "정식", "백반", "비빔밥", "쌈밥", "두부요리", "보리밥", "찌개", "전", "향토", "기사식당", "사찰음식", "국수", "칼국수", "막국수");
    private static final List<String> YASIK = List.of("야식");
    private static final List<String> BUNSIK = List.of("짬떡", "떡볶이", "떡뽀끼", "떡볶이앤카페", "국물떡볶이", "신가네", "스푼떡볶이", "달떡", "앙마", "자매떡볶이", "달볶이", "인정국물", "신떡", "분식", "김밥", "종합분식", "감성분식", "주먹밥", "떡", "찐빵", "튀김", "문빵", "샌드위치", "라면", "도시락", "밥버거");
    private static final List<String> GOGI = List.of("돼지", "오리", "소고기", "육류", "곱창", "양갈비", "양푼이막창", "정육식당", "불닭", "갈비", "고기", "양꼬치");
    private static final List<String> HAESANMUL = List.of("매운탕", "해물", "오징어", "낙지", "주꾸미", "조개", "대게", "랍스타", "전복", "굴", "복어", "킹크랩", "바닷가재", "해산물", "생선회", "아귀찜");
    private static final List<String> JJIM_TANG = List.of("찜", "탕", "감자탕", "국밥", "곰탕", "추어탕", "갈비탕", "해장국", "부대찌개", "뼈다귀탕", "신사골");
    private static final List<String> JOKBAL_BOSSAM = List.of("족발", "보쌈");
    private static final List<String> CHICKEN = List.of("치킨", "닭강정", "후라이드", "통닭");
    private static final List<String> HAMBURGER = List.of("햄버거");
    private static final List<String> PIZZA = List.of("피자");
    private static final List<String> CAFE_DESSERT = List.of("차", "카페", "커피", "와플", "빙수", "케이크", "초콜릿", "마카롱", "크레페", "디저트", "브런치", "떡카페", "ABC커피", "베이커리", "호떡", "핫도그", "도넛", "호두과자", "베이글", "후렌치후라이", "과일");
    private static final List<String> JUNGSIK = List.of("중식", "중식당", "마라", "딤섬");
    private static final List<String> ILSIK = List.of("일식", "돈까스", "초밥", "우동", "라멘", "사케");
    private static final List<String> YANGSIK = List.of("이탈리아", "스파게티", "파스타", "양식");
    private static final List<String> ASIAN = List.of("태국", "베트남", "아시아", "미스포");
    private static final List<String> GLOBAL = List.of("프랑스", "독일", "스페인", "그리스", "아메리칸", "퓨전", "멕시코", "존슨", "인도", "터키", "아프리카", "이북음식");
    private static final List<String> JUK = List.of("죽");

    public static String getCategory(String category) {
        if (matches(HANSIK, category)) return "한식";
        if (matches(BUNSIK, category)) return "분식";
        if (matches(YASIK, category)) return "야식";
        if (matches(GOGI, category)) return "고기";
        if (matches(HAESANMUL, category)) return "해산물";
        if (matches(JJIM_TANG, category)) return "찜/탕";
        if (matches(JOKBAL_BOSSAM, category)) return "족발/보쌈";
        if (matches(CHICKEN, category)) return "치킨";
        if (matches(HAMBURGER, category)) return "햄버거";
        if (matches(PIZZA, category)) return "피자";
        if (matches(CAFE_DESSERT, category)) return "카페/디저트";
        if (matches(JUNGSIK, category)) return "중식";
        if (matches(ILSIK, category)) return "일식";
        if (matches(YANGSIK, category)) return "양식";
        if (matches(ASIAN, category)) return "아시안";
        if (matches(GLOBAL, category)) return "세계음식";
        if (matches(JUK, category)) return "죽";

        return "기타";
    }

    private static boolean matches(List<String> keywords, String category) {
        return keywords.stream().anyMatch(category::contains);
    }
}
