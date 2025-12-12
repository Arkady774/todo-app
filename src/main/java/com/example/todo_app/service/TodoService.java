package com.example.todo_app.service;

import com.example.todo_app.model.TodoItem;
import com.example.todo_app.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoItem> getAll() {
        return todoRepository.findAll();
    }

    public List<TodoItem> getByDate(LocalDate date) {
        return todoRepository.findByDate(date);
    }

    public TodoItem getById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Todo item not found with id: " + id
                ));
    }

    public TodoItem create(TodoItem todoItem) {
        todoItem.setCompleted(false);
        return todoRepository.save(todoItem);
    }

    public TodoItem toggle(Long id) {
        TodoItem item = getById(id);

        boolean newCompletedStatus = !item.isCompleted();
        todoRepository.updateCompletedStatus(id, newCompletedStatus);

        item.setCompleted(newCompletedStatus);
        return item;
    }
}