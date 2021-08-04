package com.haitao.javawebproject.repository;

import com.haitao.javawebproject.pojo.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
