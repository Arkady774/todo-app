package com.example.todo_app.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TodoItemResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private boolean completed;
}