package com.haitao.javawebproject.repository;


import com.haitao.javawebproject.pojo.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {

}
