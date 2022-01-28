package ru.javabegin.backend.todo.controllers;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.todo.entity.Task;
import ru.javabegin.backend.todo.search.TaskSearchValues;
import ru.javabegin.backend.todo.service.TaskService;

import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/task")
public class TaskController {

    private static final String ID_COLUMN = "id";
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping("/email")
    public ResponseEntity findByEmail(@RequestBody String email){
        if(email == null || email.trim().length() == 0){
            return new ResponseEntity("Missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @PostMapping("/id")
    public ResponseEntity findById(@RequestBody Long id){
        Task task = null;
        try{
            task = service.findById(id);
        }catch(NoSuchElementException w){
            w.printStackTrace();
            return new ResponseEntity("task not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        try{
            service.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            e.printStackTrace();
            return new ResponseEntity("id not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity addTask(@RequestBody Task task){
        if(task.getId()!=null && task.getId()!=0){
            return new ResponseEntity("ID must by empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if(task.getTitle()==null || task.getTitle().trim().length()==0){
            return new ResponseEntity("Title cannot by empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.add(task));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Task task){
        if(task.getId()==0 || task.getId()==null){
            return new ResponseEntity("Task with id: " + task.getId() + "not found", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle()==null || task.getTitle().trim().length()==0){
            return new ResponseEntity("Title cannot be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        service.update(task);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues){

        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;
        Boolean completed = taskSearchValues.getCompleted() != null && taskSearchValues.getCompleted() == 1 ? true : false;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;
        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
        String email = taskSearchValues.getEmail() != null ? taskSearchValues.getEmail() : null;


        Date dateFrom = null;
        Date dateTo = null;

        if(email == null || email.trim().length()==0){
            return new ResponseEntity("email is empty", HttpStatus.NOT_ACCEPTABLE);
        }

        Integer pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : null;
        Integer pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

        if(taskSearchValues.getFromDate()!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(taskSearchValues.getFromDate());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND, 1);
            calendar.set(Calendar.MILLISECOND, 1);
            dateFrom = calendar.getTime();
        }

        if(taskSearchValues.getToDate()!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(taskSearchValues.getToDate());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            dateTo = calendar.getTime();
        }

        // направление сортировки
        Sort.Direction direction = sortDirection == null||sortDirection.trim().length()==0||
                sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        /* Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
            Например, если у 2-х задач одинаковое значение приоритета и мы сортируем по этому полю.
            Порядок следования этих 2-х записей после выполнения запроса может каждый раз меняться,
            т.к. не указано второе поле сортировки.
            Поэтому и используем ID - тогда все записи с одинаковым значением приоритета будут следовать
            в одном порядке по ID.
         */

        // объект сортировки, который содержит стобец и направление
        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);


        // объект постраничности
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);


        // результат запроса с постраничным выводом
        Page<Task> result = service.searchByParam(title, completed, categoryId, priorityId, email, dateFrom, dateTo, pageRequest);
        return ResponseEntity.ok(result);
    }

}
