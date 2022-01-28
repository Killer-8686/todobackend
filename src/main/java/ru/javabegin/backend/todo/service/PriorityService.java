package ru.javabegin.backend.todo.service;


import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.todo.entity.Priority;
import ru.javabegin.backend.todo.repo.PriorityRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PriorityService {

    private final PriorityRepository repository;

    public PriorityService(PriorityRepository repository) {
        this.repository = repository;
    }

    public List<Priority> findAll(String email){
        return repository.findByUserEmailOrderByTitleAsc(email);
    }

    public Priority add(Priority priority){
        return repository.save(priority);
    }

    public Priority update(Priority priority){
        return repository.save(priority);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public List<Priority> findByEmail(@Param("email") String email, @Param("title") String title){
        return repository.findByEmail(email, title);
    }

    public Priority findById(Long id){
        return repository.findById(id).get();
    }
}

