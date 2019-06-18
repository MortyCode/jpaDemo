package com.jpatest.jpaTest;

import com.jpatest.jpaTest.entity.Account;
import com.jpatest.jpaTest.query.QAccount;
import com.jpatest.jpaTest.repository.AccountRepository;
import com.jpatest.jpaTest.service.AccountServiceImpl;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.querydsl.QSort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaTestApplicationTests {


	@Resource
	AccountRepository repository;
	@Resource
	JPAQueryFactory jpaQueryFactory;
	@Resource
	EntityManager entityManager;
	@Resource
	AccountServiceImpl accountService;

	@Test
	public void save() {

			Account account = new Account();
			account.setAge(1);
			account.setCreateTime(new Date());
			account.setDelete(true);
			account.setFirstName("liru");
			account.setLastName("雷神");
			account.setPassword("12345");
			account = repository.save(account);
			System.out.println(account);

			account.setLastName("徐锦江");

			account = repository.save(account);
			System.out.println(account);



		Account one = repository.getOne(account.getId());

		one.setLastName("李世民");
		repository.saveAndFlush(one);


		Optional<Account> byId = repository.findById(account.getId());

		System.out.println(byId.get());


	}





	/**
	 * 第一种，根据方法名查询
	 */
	@Test
	@Transactional(readOnly = true)
	public void query1(){

		Sort sort = new Sort(Sort.Direction.DESC,"age");
		PageRequest pageRequest = PageRequest.of(1,2,sort);

		List<Account> allByAgeIs = repository.findAllByAgeIs(1);
		System.out.println(allByAgeIs);

		Page<Account> allByAgeIs2 = repository.findAllByAgeIs(1, pageRequest);
		System.out.println(allByAgeIs2.getContent());
		System.out.println(allByAgeIs2.getSize());
		System.out.println(allByAgeIs2.getTotalElements());
		System.out.println(allByAgeIs2.getTotalPages());
		System.out.println(allByAgeIs2.getNumber());
		System.out.println(allByAgeIs2.getNumberOfElements());

		Slice<Account> slice = repository.findAllByAgeIsOrderByAgeDesc(1, pageRequest);
		System.out.println(slice.getContent());


		List<Account> allByAgeIs1 = repository.findFirst1ByAgeIs(1);
		System.out.println(allByAgeIs1);

		Account top = repository.findTopByAgeIs(1);
		System.out.println(top);

		List<Account> allByAgeIn = repository.findAllByAgeInOrderByAgeAsc(Lists.newArrayList(1, 2));
		System.out.println(allByAgeIn);


		Stream<Account> accountStream = repository.readAllByAgeIs(1);
		accountStream.forEach(e->{
			System.out.println("stream"+e);
		});

		accountStream.close();

	}

	/**
	 * 第二种，使用@Query注解查询
	 */
	@Test
	public void query2(){

		List<Account> l1 = repository.findAllByFname("%l%");
		System.out.println(l1);

		List<Object> allByFnameObject = repository.findAllByFnameObject("%l%");


		List<Account> allBySql = repository.findAllBySql("%l%");
		System.out.println(allBySql);

		List<Account> allByFnameSpel = repository.findAllByFnameSpel("%l%");
		System.out.println("spel "+ allByFnameSpel);


		List<Account> accounts = repository.findbyParam("%l%");
		System.out.println(accounts);

		repository.updateLname("老鼠", 1L);

		Optional<Account> byId = repository.findById(1L);
		System.out.println(byId.get());

		Query query = entityManager.createNamedQuery("queryAge");
		query.setParameter(1,1);
		List resultList = query.getResultList();
		System.out.println("em  "+  resultList);

	}

	/**
	 * 第三种，使用Example查询
	 */
	@Test
	public void query3(){

		//1.组成查询实体
		Account account = new Account();
		account.setAge(1);
		account.setFirstName("a");
		account.setDelete(true);
		//2.组成匹配方式
		ExampleMatcher exampleMatcher =
				ExampleMatcher.matching().
						withMatcher("firstName",e->e.contains());
		//3.组合example
		Example<Account> example = Example.of(account,exampleMatcher);
		//4.如需要分页和排序，则创建分页和排序
		Sort sort = new Sort(Sort.Direction.DESC,"age");
		PageRequest pageRequest = PageRequest.of(0,10,sort);
		//5.查询
		Page<Account> all1 = repository.findAll(example, pageRequest);

		System.out.println(all1.getContent());



		Account account2 = new Account();
		account.setFirstName("a");
		ExampleMatcher exampleMatcher2 = ExampleMatcher.matching().withMatcher("firstName",e->e.contains());
		Example<Account> example2 = Example.of(account2,exampleMatcher2);

		Page<Account> all2 = repository.findAll(example2, pageRequest);

		System.out.println("example2:   "+ all2.getContent());



	}


	/**
	 * 第四种，使用Specification
	 */
	@Test
	public void query4(){

		Sort sort = new Sort(Sort.Direction.DESC,"age");
		PageRequest pageRequest = PageRequest.of(0,1,sort);

		Page<Account> age = repository.findAll((root, criteriaQuery, criteriaBuilder) -> {

			List<javax.persistence.criteria.Predicate> list = new ArrayList<>();

			list.add(criteriaBuilder.equal(root.get("age"), "1"));

			javax.persistence.criteria.Predicate[] p = new javax.persistence.criteria.Predicate[list.size()];
			return criteriaBuilder.and(list.toArray(p));

		}, pageRequest);


		System.out.println(age.getContent());
	}

	/**
	 * 第五种 使用querydsl查询，1.
	 */
	public void query5(){

		//1. 使用findall查询
		QAccount qaccount = QAccount.account;
		PageRequest qpageRequest = PageRequest.of(0,1,new QSort(qaccount.age.asc()));
		Predicate predicate = qaccount.age.eq(1);
		Iterable<Account> all = repository.findAll(predicate,qpageRequest);
		long count = repository.count(predicate);
		System.out.println(all);
		System.out.println(count);

		String s = qaccount.age.toString();
		System.out.println(s);


		//2. 使用jpaQueryFactory
		List<Account> fetch = jpaQueryFactory.selectFrom(qaccount).where(qaccount.age.eq(1)).fetch();
		long l = jpaQueryFactory.selectFrom(qaccount).where(qaccount.age.eq(1)).fetchCount();
		System.out.println(fetch);
		System.out.println(l);

		List<Tuple> fetch1 = jpaQueryFactory.select(qaccount.age, qaccount.firstName).from(qaccount).fetch();
		System.out.println(fetch1);

		String firstName = fetch1.get(0).get(qaccount.firstName);
		System.out.println(firstName);
	}

}
