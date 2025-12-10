package com.example.todo_app.controller;

import com.example.todo_app.model.TodoItem;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TodosController {

    private List<TodoItem> todoItems = new ArrayList<>();

    @GetMapping("/tasks")
    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    @GetMapping("/tasks/filtered")
    public List<TodoItem> getTodoItemsFiltered(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        List<TodoItem> filteredTodoItems = new ArrayList<>();
        for(TodoItem item : todoItems) {
            if(item.getDate().equals(date)) {
                filteredTodoItems.add(item);
            }
        }
        return filteredTodoItems;
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
