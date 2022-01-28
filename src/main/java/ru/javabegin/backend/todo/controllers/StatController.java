package ru.javabegin.backend.todo.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.backend.todo.entity.Stat;
import ru.javabegin.backend.todo.repo.StatRepository;

@RestController
public class StatController {

    private final StatRepository repository;

    public StatController(StatRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody String email){
        return ResponseEntity.ok(repository.findByUserEmail(email));
    }
}
