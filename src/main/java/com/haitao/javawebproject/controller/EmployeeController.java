package com.haitao.javawebproject.controller;


import com.haitao.javawebproject.exception.EmployeeNotFoundException;
import com.haitao.javawebproject.pojo.Employee;
import com.haitao.javawebproject.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository){
        this.repository=repository;
    }

    @GetMapping("/employees")
    List<Employee> all(){
        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee){
        return repository.save(newEmployee);
    }

    /**
     * single item
     * @param id
     * @return
     */
    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id){
        return repository.findById(id).orElseThrow(()->new EmployeeNotFoundException(id));
    }

    /**
     * 修改指定id的employees的属性
     * @return
     */
//    @PutMapping("/employees/{id}")
//    Employee replaceEmployee(@PathVariable Long id,@RequestBody Employee employee){
//        employee.setId(id);
//        return repository.save(employee);
//    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
    }


}
