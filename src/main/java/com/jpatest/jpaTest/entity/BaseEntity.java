package com.jpatest.jpaTest.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 * xuanwu （yule@txp-life.com）
 * 2019-04-25
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

    /**
     * 自动增长ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 是否删除 1：是，0：否
     */
    @Column(length = 2)
    private String isDeleted;

    /**
     * sellerId
     */
    @Column(length = 20)
    private long sellerId;

    /**
     * 创建时间
     */
    @Column
    private Date createTime;

    /**
     * 更新时间
     */
    @Column
    private Date updateTime;

    /**
     * 创建者ID
     */
    @Column(length = 20)
    private long createId;

    /**
     * 创建者姓名
     */
    @Column(length = 50)
    private String createName;

    /**
     * 更新者ID
     */
    @Column(length = 20)
    private long updateId;

    /**
     * 更新者姓名
     */
    @Column(length = 50)
    private String updateName;

}
