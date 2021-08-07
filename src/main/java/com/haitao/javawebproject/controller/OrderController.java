package com.haitao.javawebproject.controller;


import com.haitao.javawebproject.aspect.assembler.OrderMoelAssembler;
import com.haitao.javawebproject.exception.OrderNotFoundException;
import com.haitao.javawebproject.pojo.Order;
import com.haitao.javawebproject.pojo.Status;
import com.haitao.javawebproject.repository.OrderRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderMoelAssembler assembler;

    public OrderController(OrderRepository orderRepository, OrderMoelAssembler assembler) {
        this.orderRepository = orderRepository;
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>>  all(){
        List<EntityModel<Order>> orders = orderRepository.findAll().stream().
                map(assembler::toModel).collect(Collectors.toList());
        return  CollectionModel.of(orders,linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order>  one(@PathVariable Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
      return assembler.toModel(order);
    }


    @PostMapping("/orders")
    ResponseEntity newOrder(@RequestBody Order order){
        order.setStatus(Status.IN_PROGRESS);
        Order save = orderRepository.save(order);
        EntityModel<Order> orderEntityModel = assembler.toModel(save);
       return  ResponseEntity.created(orderEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(orderEntityModel);

    }


}
