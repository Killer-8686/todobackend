package ru.javabegin.backend.todo.service;


import org.springframework.stereotype.Service;
import ru.javabegin.backend.todo.entity.Category;
import ru.javabegin.backend.todo.repo.CategoryRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service

@Transactional
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll(String email){
        return repository.findByUserEmailOrderByTitleAsc(email);
    }

    public Category add(Category category){
        return repository.save(category);
    }

    public Category update(Category category){
        return repository.save(category);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public List<Category> findByTitle(String title, String email){
        return repository.findByTitle(title, email);
    }

    public Category findById(Long id){
        return repository.findById(id).get();// возвращается Optional, поэтому get()
    }
}
