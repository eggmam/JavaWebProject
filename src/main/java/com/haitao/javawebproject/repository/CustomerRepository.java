package com.haitao.javawebproject.repository;

import com.haitao.javawebproject.pojo.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CurdRepository extends CrudRepository<Customer,Long> {
    List<Customer> findByLa

}
