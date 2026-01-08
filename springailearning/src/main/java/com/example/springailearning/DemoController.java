package com.example.springailearning;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/greeting")
    public String getGreeting() {
        return "Hello from Spring Boot!";
    }

    @GetMapping("/greet/{name}")
    public String greetUser(@PathVariable String name) {
        return String.format("Hello, %s! Welcome to Spring Boot!", name);
    }

    @PostMapping("/greeting")
    public String createGreeting(@RequestParam String name) {
        return String.format("Created a new greeting for %s", name);
    }

    @PutMapping("/greeting/{id}")
    public String updateGreeting(@PathVariable Long id, @RequestParam String message) {
        return String.format("Updated greeting with id %d to: %s", id, message);
    }

    @DeleteMapping("/greeting/{id}")
    public String deleteGreeting(@PathVariable Long id) {
        return String.format("Deleted greeting with id %d", id);
    }
}
