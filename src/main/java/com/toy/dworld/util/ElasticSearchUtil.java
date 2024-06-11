package com.toy.dworld.util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.Arrays;
import java.util.function.Supplier;

public class ElasticSearchUtil {

    public static Supplier<Query> supplier(){ // 쿼리 공급 함수
        Supplier<Query> supplier = ()->Query.of(q->q.matchAll(matchAllQuery()));  //Query.of() : Elasticsearch에서 쿼리를 생성하기 위한 팩토리 메서드
        return supplier;
    }
    public static MatchAllQuery matchAllQuery(){  // matchAll 쿼리
        return new MatchAllQuery.Builder().build();
    }

    public static Supplier<Query> supplierWithKeyword(String fieldValue){ // 쿼리 공급 함수 (매개변수 name인 경우) -> name 필드 값이 매개변수값과 같은 경우 쿼리
        //Supplier<Query> supplier = ()->Query.of(q->q.match(matchQueryWithFieldName(fieldValue)));
        return ()->Query.of(q->q.multiMatch(matchQueryWithFieldName(fieldValue)));
    }
    public static MultiMatchQuery matchQueryWithFieldName(String fieldValue){  // match 쿼리 , 바로 위에서 참조중
        return new MultiMatchQuery.Builder().fields(Arrays.asList("title", "content")).query(fieldValue).build();
    }

}
