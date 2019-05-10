package com.jpatest.jpaTest.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@EnableJpaRepositories(basePackages = {"com.jpatest.jpaTest.repository"})
@EntityScan("com.jpatest.jpaTest.entity")
@Configuration  // 这个注解必须加
public class JPAConfig {

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }


    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){

        return new JPAQueryFactory(entityManager);
    }

}
