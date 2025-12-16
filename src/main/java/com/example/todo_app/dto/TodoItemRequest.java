package com.example.todo_app.dto;

import com.example.todo_app.model.TodoItem;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TodoItemRequest {
    private String title;
    private String description;
    private LocalDate date;

    public TodoItem toEntity() {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(this.title);
        todoItem.setDescription(this.description);
        todoItem.setDate(this.date);
        todoItem.setCompleted(false); // По умолчанию не выполнено
        return todoItem;
    }
}