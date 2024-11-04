package org.una.programmingIII.Assignment_Manager.Controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class HelloWorldController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }

    @GetMapping
    public String getAll() {
        return "All!";
    }

    @PostMapping
    public String create(@RequestBody String test) {
        return "Create " + test;
    }
}
