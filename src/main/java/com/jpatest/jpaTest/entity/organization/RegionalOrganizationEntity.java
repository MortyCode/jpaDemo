package com.jpatest.jpaTest.entity.organization;

import com.jpatest.jpaTest.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Data
@Table(name = "regional_organization")
public class RegionalOrganizationEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1255384735586566958L;

    @Column
    private String organizationName;

    @Column
    private int parentId;

    @Column
    private String path;

    /**
     * 等级类型
     */
    @Column
    private String levelType;
}
