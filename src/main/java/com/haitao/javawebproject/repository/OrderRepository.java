package com.haitao.javawebproject.repository;

import com.haitao.javawebproject.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
