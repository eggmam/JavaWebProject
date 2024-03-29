package com.haitao.javawebproject.controller;

import com.haitao.javawebproject.pojo.Greeting;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting (@RequestParam(value="name",defaultValue = "world") String name){
        return new Greeting(counter.incrementAndGet(),String.format("Hello, %s!", name));
    }
}
