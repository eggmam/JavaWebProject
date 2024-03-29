package com.haitao.javawebproject.controller;


import com.haitao.javawebproject.aspect.assembler.OrderMoelAssembler;
import com.haitao.javawebproject.exception.OrderNotFoundException;
import com.haitao.javawebproject.pojo.Order;
import com.haitao.javawebproject.pojo.Status;
import com.haitao.javawebproject.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity newOrder(@RequestBody Order order){
        order.setStatus(Status.IN_PROGRESS);
        Order save = orderRepository.save(order);
        EntityModel<Order> orderEntityModel = assembler.toModel(save);
       return  ResponseEntity.created(orderEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(orderEntityModel);

    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity cancel(@PathVariable Long id){
        Order order = orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException(id));
        if (order.getStatus()==Status.IN_PROGRESS){
            order.setStatus(Status.CANCELED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create().withTitle("Method not allowed").
                        withDetail("you can't cancel an order that is in the"+order.getStatus()+" status"));
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity complete(@PathVariable Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

       if (order.getStatus()==Status.IN_PROGRESS){
           order.setStatus(Status.COMPLETED);
           return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
       }

       return ResponseEntity
               .status(HttpStatus.METHOD_NOT_ALLOWED)
               .header(HttpHeaders.CONTENT_TYPE,MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
               .body(Problem.create()
                       .withTitle("Method not allowed")
                       .withDetail("You can't complete an order that is in the "+order.getStatus()+"status"));
    }


}
