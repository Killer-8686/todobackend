package ru.javabegin.backend.todo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.todo.entity.Priority;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findByUserEmailOrderByTitleAsc(String email);


    @Query("SELECT p FROM Priority p WHERE " +
            "(:title is null OR :title='' " +
            "OR lower(p.title) like lower(concat('%', :title, '%'))) " +
            "AND p.user.email = :email " +
            "ORDER BY p.title ASC")
    List<Priority> findByEmail(String email, String title);
}
