package com.example.todo_app.controller;

import com.example.todo_app.model.TodoItem;
import com.example.todo_app.service.TodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TodosController {

    private final TodoService todoService;

    public TodosController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/tasks")
    public List<TodoItem> getTodoItems() {
        return todoService.getAll();
    }

    @GetMapping("/tasks/filtered")
    public List<TodoItem> getTodoItemsFiltered(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return todoService.getByDate(date);
    }

    @GetMapping("/tasks/{id}")
    public TodoItem getItemById(@PathVariable Long id) {
        return todoService.getById(id);
    }

    @PostMapping("/tasks")
    public TodoItem createTodoItem(@RequestBody TodoItem todoItem) {
        return todoService.create(todoItem);
    }

    @PostMapping("/tasks/{id}/toggle")
    public TodoItem toggle(@PathVariable Long id) {
        return todoService.toggle(id);
    }
}
