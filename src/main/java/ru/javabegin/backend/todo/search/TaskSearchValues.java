package ru.javabegin.backend.todo.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchValues {

    private String title;
    private Integer completed;
    private Long categoryId;
    private Long priorityId;
    private String email;
    private Date fromDate; //поиск среази задач от этого времени
    private Date toDate; // до этого

    //Постраничность

    private Integer pageNumber;
    private Integer pageSize;

    //сортировка

    private String sortColumn;
    private String sortDirection;

}
