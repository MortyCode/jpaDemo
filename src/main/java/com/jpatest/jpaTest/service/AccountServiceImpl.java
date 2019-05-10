package com.jpatest.jpaTest.service;

import org.springframework.stereotype.Service;

@Service("AccountServiceImpl2")
public class AccountServiceImpl implements AccountService{
    @Override
    public String getImpl() {
        return "第一实现";
    }
}
