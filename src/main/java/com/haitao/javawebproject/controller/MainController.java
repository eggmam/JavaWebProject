package com.haitao.javawebproject.controller;


import com.haitao.javawebproject.repository.UserRepository;
import com.haitao.javawebproject.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/demo")
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public String addNewUser (@RequestParam String name
            , @RequestParam String email) {

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    public  Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping(path="/delete")
    public  String  deleteById(@RequestParam Integer id) {
        User n = new User();
        n.setId(id);
        userRepository.delete(n);
        return "delete";
    }
}
