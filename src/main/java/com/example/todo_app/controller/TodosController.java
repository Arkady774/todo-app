package com.example.todo_app.controller;

import com.example.todo_app.model.TodoItem;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TodosController {

    private List<TodoItem> todoItems = new ArrayList<>();

    @GetMapping("/tasks")
    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    @GetMapping("/tasks/{id}")
    public TodoItem getItemById(@PathVariable Long id) {
        //Найти в todoItems задачу с айдишником id и вернуть
        for (TodoItem item : todoItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    @PostMapping("/tasks")
    public TodoItem createTodoItem(@RequestBody TodoItem todoItem) {
        todoItem.setId((long) todoItems.size() + 1);
        todoItems.add(todoItem);
        return todoItem;
    }

    @PostMapping("/tasks/{id}/toggle")
    public TodoItem toggle(@PathVariable Long id) {
        for (TodoItem item : todoItems) {
            if (item.getId().equals(id)) {
                item.setCompleted(!item.isCompleted());
                return item;
            }
        }
        return null;

    }

}
