package com.jpatest.jpaTest.query;

import com.jpatest.jpaTest.entity.Account;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = 1992732209L;

    public static final QAccount account = new QAccount("account");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDelete = createBoolean("isDelete");

    public final StringPath lastName = createString("lastName");

    public final StringPath password = createString("password");

    public QAccount(String variable) {
        super(Account.class, forVariable(variable));
    }

    public QAccount(Path<? extends Account> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccount(PathMetadata metadata) {
        super(Account.class, metadata);
    }

}

