package com.haitao.javawebproject.controller;


import com.haitao.javawebproject.aspect.assembler.EmployeeModelAssembler;
import com.haitao.javawebproject.exception.EmployeeNotFoundException;
import com.haitao.javawebproject.pojo.Employee;
import com.haitao.javawebproject.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final EmployeeModelAssembler assembler;

    EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler){
        this.repository=repository;
        this.assembler = assembler;
    }


    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all(){
        List<EntityModel<Employee>> employees = repository.findAll().stream().
                map(assembler::toModel)
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
    public EntityModel<Employee> one(@PathVariable Long id){
        Employee employee =repository.findById(id).orElseThrow(()->new EmployeeNotFoundException(id));
        return assembler.toModel(employee);
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
