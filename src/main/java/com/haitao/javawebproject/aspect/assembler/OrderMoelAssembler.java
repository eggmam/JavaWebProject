package com.haitao.javawebproject.aspect.assembler;

import com.haitao.javawebproject.controller.EmployeeController;
import com.haitao.javawebproject.controller.OrderController;
import com.haitao.javawebproject.pojo.Employee;
import com.haitao.javawebproject.pojo.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class OrderMoelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {



    @Override
    public EntityModel<Order> toModel(Order entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(OrderController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders"));
    }
}
