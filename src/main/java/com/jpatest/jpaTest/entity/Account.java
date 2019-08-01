package com.jpatest.jpaTest.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;


@Data
@Proxy(lazy = false)
@Entity
@NamedQuery(name="queryAge",query = "from Account where age=?1")
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    @Version
    private Long version;

    @Column(name = "age")
    private int age;

    @Column(name = "passwd")
    private String password;

    @Column
    private Date createTime;

    @Column
    private boolean isDelete;

}

