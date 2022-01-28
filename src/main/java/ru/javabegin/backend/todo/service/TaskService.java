package ru.javabegin.backend.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.todo.entity.Task;
import ru.javabegin.backend.todo.repo.TaskRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findByEmail(String email){
        return repository.findByUserEmailOrderByTitleAsc(email);
    }

    public Task findById(Long id){
        return repository.findById(id).get();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Task add(Task task){
        return repository.save(task);
    }

    public void update(Task task){
        repository.save(task);
    }

    public Page<Task> searchByParam(String title, Boolean completed,Long categoryId, Long priorityId,
                                   String email, Date dateFrom, Date dateTo, PageRequest pageable){
        return repository.findByParams(title, completed, categoryId, priorityId, email, dateFrom, dateTo,pageable);
    }
}
