package ru.javabegin.backend.todo.controllers;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.todo.entity.Category;
import ru.javabegin.backend.todo.search.CategorySearchValues;
import ru.javabegin.backend.todo.service.CategoryService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/all")
    public List<Category> findAll(@RequestBody String email){
        return categoryService.findAll(email);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category){

        // Проверка на обязательные параметры

        if(category.getId()!=null && category.getId() != 0){ //если id заполнено, то не добавляем в БД так как БД сама присваивает id
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // Если передали пустое значание title
        if(category.getTitle() == null || category.getTitle().trim().length() == 0){
            return new ResponseEntity("missed param: title MUST be not empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(categoryService.add(category)); // возвращаем добавленный объект с присвоенным id
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category){

        // проверка, что бы id НЕ было пустым
        if(category.getId() == null || category.getId() == 0){
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        // Поле title  не должно быть пустым
        if(category.getTitle() == null || category.getTitle().trim().length() == 0 ){
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        categoryService.update(category);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        try{
            categoryService.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity search(@RequestBody CategorySearchValues categorySearchValues){

        if(categorySearchValues.getEmail().trim().length()==0||categorySearchValues.getEmail()==null){
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }


        List<Category> list = categoryService.findByTitle(categorySearchValues.getTitle(), categorySearchValues.getEmail());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id){
        Category category = null;

        try{
            category = categoryService.findById(id);
        } catch (NoSuchElementException e){
            e.printStackTrace();
            return new ResponseEntity("id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE );
        }
        return ResponseEntity.ok(category);
    }


}
