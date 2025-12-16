package com.example.todo_app.controller;

import com.example.todo_app.dto.TodoItemRequest;
import com.example.todo_app.dto.TodoItemResponse;
import com.example.todo_app.service.TodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TodoItemResponse>> getTodoItems() {
        List<TodoItemResponse> responses = todoService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tasks/filtered")
    public ResponseEntity<List<TodoItemResponse>> getTodoItemsFiltered(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        List<TodoItemResponse> responses = todoService.getByDate(date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TodoItemResponse> getItemById(@PathVariable Long id) {
        TodoItemResponse response = todoService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TodoItemResponse> createTodoItem(@RequestBody TodoItemRequest request) {
        TodoItemResponse response = todoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/tasks/{id}/toggle")
    public ResponseEntity<TodoItemResponse> toggle(@PathVariable Long id) {
        TodoItemResponse response = todoService.toggle(id);
        return ResponseEntity.ok(response);
    }
}