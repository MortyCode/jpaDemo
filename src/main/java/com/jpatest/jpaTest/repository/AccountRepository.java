package com.jpatest.jpaTest.repository;

import com.jpatest.jpaTest.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;


public interface AccountRepository extends JpaRepository<Account,Long> ,
        JpaSpecificationExecutor<Account> ,

        QuerydslPredicateExecutor<Account> {


    List<Account> findAllByAgeIs(int i);

    Stream<Account> readAllByAgeIs(int i);

    Page<Account> findAllByAgeIs(int i,Pageable pageable);

    Slice<Account> findAllByAgeIsOrderByAgeDesc(int i, Pageable pageable);

    List<Account> findFirst1ByAgeIs(int i);

    Account findTopByAgeIs(int i);

    List<Account> findAllByAgeInOrderByAgeAsc(List<Integer> ages);




    @Query("select a from Account a where a.firstName like ?1")
    List<Account> findAllByFname(String fname);

    @Query("select a.lastName,a.firstName from Account a where a.firstName like ?1")
    List<Object> findAllByFnameObject(String fname);

    @Query("select a from #{#entityName} a where a.firstName like ?1")
    List<Account> findAllByFnameSpel(String fname);

    @Query(value = "SELECT * FROM account WHERE first_name like ?1",nativeQuery = true)
    List<Account> findAllBySql(String fname);

    @Query("select a from Account a where a.firstName like :name")
    List<Account> findbyParam(@Param("name") String fname);

    @Transactional
    @Modifying
    @Query("update Account a set lastName =?1 where id=?2")
    int updateLname(String lname,Long id);

}
