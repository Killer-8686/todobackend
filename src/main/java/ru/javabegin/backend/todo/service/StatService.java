package ru.javabegin.backend.todo.service;

import org.springframework.stereotype.Service;
import ru.javabegin.backend.todo.entity.Stat;
import ru.javabegin.backend.todo.repo.StatRepository;

@Service
public class StatService {

    private final StatRepository statRepository;

    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public Stat findByEmail(String email){
        return statRepository.findByUserEmail(email);
    }
}
