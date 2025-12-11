package com.example.todo_app.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class TodoItem {
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    public TodoItem() {}

    public TodoItem(Long id, String title, String description, LocalDate date, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.completed = completed;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}