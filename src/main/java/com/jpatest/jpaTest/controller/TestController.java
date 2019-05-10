//package com.jpatest.jpaTest.controller;
//
//import com.querydsl.core.types.Predicate;
//import org.springframework.data.querydsl.QPageRequest;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//@RestController
//public class TestController {
//
//
//    @Resource
//    PlaceCommunityRelationRepository repository;
//
//    @RequestMapping("/test")
//    public void test(){
//        QPlaceCommunityRelationEntity entity= QPlaceCommunityRelationEntity.placeCommunityRelationEntity;
//
//        Predicate predicate = entity.id.eq(1)
//                .and(entity.communityName.contains(""))
//                .and(entity.id.in(1,2));
//
//        QPageRequest pageRequest = new QPageRequest(0,10,entity.createTime.desc());
//
//        repository.findAll(predicate,pageRequest);
//    }
//
//}
