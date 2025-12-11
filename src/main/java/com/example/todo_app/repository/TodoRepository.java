package com.example.todo_app.repository;

import com.example.todo_app.model.TodoItem;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TodoRepository {

    //TODO implement me
    public List<TodoItem> findAll() {
        return null;
    }

    //TODO implement me
    public TodoItem findById(long id) {
        return null;
    }

    //TODO implement me
    public TodoItem findByDate(LocalDate date) {
        return null;
    }

    //TODO implement me
    public TodoItem save(TodoItem item) {
        return null;
    }
}
