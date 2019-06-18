package com.jpatest.jpaTest.service;

import com.jpatest.jpaTest.entity.Account;
import com.jpatest.jpaTest.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("AccountServiceImpl2")
public class AccountServiceImpl implements AccountService{
    @Override
    public String getImpl() {
        return "第一实现";
    }


    @Resource
    AccountRepository repository;

    @Transactional
    public void update(Account account){

        account.setFirstName("1111111112");

        update2(account);

    }

    public void update2(Account account){
        repository.save(account);

    }
}
