package com.example.todo_app.mapper;

import com.example.todo_app.dto.TodoItemRequest;
import com.example.todo_app.dto.TodoItemResponse;
import com.example.todo_app.model.TodoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(target = "completed", constant = "false")
    TodoItem toEntity(TodoItemRequest request);

    TodoItemResponse toResponse(TodoItem entity);
}