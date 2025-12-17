package com.example.todo_app.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TodoItemRequest {
    private String title;
    private String description;
    private LocalDate date;
}