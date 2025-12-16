package com.example.todo_app.repository;

import com.example.todo_app.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TodoRepositoryJPA extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByDate(LocalDate date);


}