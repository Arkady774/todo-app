package com.example.todo_app.service;

import com.example.todo_app.model.TodoItem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {

    private List<TodoItem> todoItems = new ArrayList<>();

    public List<TodoItem> getAll() {
        return todoItems;
    }

    public List<TodoItem> getByDate(LocalDate date) {
        List<TodoItem> result = new ArrayList<>();
        for (TodoItem item : todoItems) {
            if (item.getDate().equals(date)) {
                result.add(item);
            }
        }
        return result;
    }

    public TodoItem getById(Long id) {
        for (TodoItem item : todoItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }

    public TodoItem create(TodoItem todoItem) {
        todoItem.setId((long) todoItems.size() + 1);
        todoItems.add(todoItem);
        return todoItem;
    }

    public TodoItem toggle(Long id) {
        for (TodoItem item : todoItems) {
            if (item.getId().equals(id)) {
                item.setCompleted(!item.isCompleted());
                return item;
            }
        }
        return null;
    }

}

