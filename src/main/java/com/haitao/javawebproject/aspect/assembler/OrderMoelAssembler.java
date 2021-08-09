package com.haitao.javawebproject.aspect.assembler;

import com.haitao.javawebproject.controller.EmployeeController;
import com.haitao.javawebproject.controller.OrderController;
import com.haitao.javawebproject.pojo.Employee;
import com.haitao.javawebproject.pojo.Order;
import com.haitao.javawebproject.pojo.Status;
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
        //Unconditional links to single-item resource and aggregate root
        EntityModel<Order> orderModel = EntityModel.of(entity,
                linkTo(methodOn(OrderController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders"));

        //conditional links based on state of the order
        if(entity.getStatus()== Status.IN_PROGRESS){
            orderModel.add(linkTo(methodOn(OrderController.class).cancel(entity.getId())).withRel("cancel"));
            orderModel.add(linkTo(methodOn(OrderController.class).complete(entity.getId())).withRel("complete"));
        }

        return orderModel;
    }
}
