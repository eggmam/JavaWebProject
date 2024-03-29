package com.haitao.javawebproject.aspect.assembler;


import com.haitao.javawebproject.controller.EmployeeController;
import com.haitao.javawebproject.pojo.Employee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {


    /**
     * 将employee包装为EntityModel<Employee>。
     * @param entity
     * @return  新的对象增加了_links属性
     */
    @Override
    public EntityModel<Employee> toModel(Employee entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(EmployeeController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }
}
