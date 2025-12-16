package com.example.todo_app.dto;

import com.example.todo_app.model.TodoItem;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TodoItemResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private boolean completed;

    public static TodoItemResponse fromEntity(TodoItem todoItem) {
        TodoItemResponse response = new TodoItemResponse();
        response.setId(todoItem.getId());
        response.setTitle(todoItem.getTitle());
        response.setDescription(todoItem.getDescription());
        response.setDate(todoItem.getDate());
        response.setCompleted(todoItem.isCompleted());
        return response;
    }
}