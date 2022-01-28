package ru.javabegin.backend.todo.controllers;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.todo.entity.Priority;
import ru.javabegin.backend.todo.search.PrioritySearchValues;
import ru.javabegin.backend.todo.service.PriorityService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityService service;

    public PriorityController(PriorityService service) {
        this.service = service;
    }

    @RequestMapping("/all")
    public List<Priority> findAll(@RequestBody String email){
        return service.findAll(email);
    }

    @RequestMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority){

        if(priority.getId()!= null && priority.getId() != 0){
            return new ResponseEntity("Priority ID must be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if(priority.getTitle().trim().length()==0 || priority.getTitle()==null){
            return new ResponseEntity("Title cannot be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if(priority.getColor() == null){
            return new ResponseEntity("Color cannot by empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.add(priority));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority){

        if(priority.getId()==0 || priority.getId() == null){
            return new ResponseEntity("Missed param : id", HttpStatus.NOT_ACCEPTABLE);
        }
        if(priority.getTitle()==null || priority.getTitle().trim().length()==0){
            return new ResponseEntity("Missed param : title, cannot by empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if(priority.getColor()== null){
            return new ResponseEntity("Missed param : color cannot must by empty", HttpStatus.NOT_ACCEPTABLE);
        }

        service.update(priority);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        try{
            service.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return new ResponseEntity("priority id not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity findByEmail(@RequestBody PrioritySearchValues prioritySearchValues){
        if(prioritySearchValues.getEmail()==null || prioritySearchValues.getEmail().trim().length()==0){
            return new ResponseEntity("Missed param email", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Priority> list = service.findByEmail(prioritySearchValues.getEmail(), prioritySearchValues.getTitle());

        return ResponseEntity.ok(list);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long id){
        Priority priority = null;

        try{
            priority = service.findById(id);
        } catch(NoSuchElementException e){
            e.printStackTrace();
            return new ResponseEntity("id not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(priority);
    }
}
