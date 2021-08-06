package com.haitao.javawebproject.controller;


import com.haitao.javawebproject.exception.EmployeeNotFoundException;
import com.haitao.javawebproject.pojo.Employee;
import com.haitao.javawebproject.repository.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository){
        this.repository=repository;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all(){
        List<EntityModel<Employee>> employees = repository.findAll().stream().
                map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
                .collect(Collectors.toList());
        return CollectionModel.of(employees,linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
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
    EntityModel<Employee> one(@PathVariable Long id){
        Employee employee =repository.findById(id).orElseThrow(()->new EmployeeNotFoundException(id));
        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
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
