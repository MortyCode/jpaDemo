package com.jpatest.jpaTest.entity.organization;

import com.jpatest.jpaTest.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 卸货地点与社区关联关系
 * xuanwu （yule@txp-life.com）
 * 2019-04-25
 */
@Entity
@Data
@Table(name = "regional_organization_account_relation")
public class RegionalOrganizationAccountRelationEntity extends BaseEntity implements Serializable {

    /**
     * 卸货地点ID
     */
    @Column
    private int regionalId;

    /**
     * 卸货地点名称
     */
    @Column
    private Long accountId;

}
