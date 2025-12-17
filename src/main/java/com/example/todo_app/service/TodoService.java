package com.example.todo_app.service;

import com.example.todo_app.dto.TodoItemRequest;
import com.example.todo_app.dto.TodoItemResponse;
import com.example.todo_app.mapper.TodoMapper;
import com.example.todo_app.model.TodoItem;
import com.example.todo_app.repository.TodoRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TodoService {

    private final TodoRepositoryJPA todoRepository;
    private final TodoMapper todoMapper;

    @Autowired
    public TodoService(TodoRepositoryJPA todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
        log.info("TodoService инициализирован с репозиторием");
    }

    public List<TodoItemResponse> getAll() {
        log.info("Получение всех задач");
        List<TodoItem> items = todoRepository.findAll();
        log.info("Получено {} задач", items.size());
        return items.stream()
                .map(todoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<TodoItemResponse> getByDate(LocalDate date) {
        log.info("Получение задач на дату: {}", date);
        List<TodoItem> items = todoRepository.findByDate(date);
        log.info("Получено {} задач на дату: {}", items.size(), date);
        return items.stream()
                .map(todoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TodoItemResponse getById(Long id) {
        log.info("Получение задачи по id: {}", id);
        try {
            TodoItem item = todoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Задача не найдена с id: {}", id);
                        return new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Todo item not found with id: " + id
                        );
                    });
            log.info("Задача найдена с id: {}", id);
            return todoMapper.toResponse(item);
        } catch (ResponseStatusException ex) {
            log.error("Не удалось получить задачу с id: {}, ошибка: {}", id, ex.getMessage());
            throw ex;
        }
    }

    public TodoItemResponse create(TodoItemRequest request) {
        log.info("Создание новой задачи с заголовком: {}", request.getTitle());
        TodoItem todoItem = todoMapper.toEntity(request);
        TodoItem savedItem = todoRepository.save(todoItem);
        log.info("Задача успешно создана с id: {}", savedItem.getId());
        return todoMapper.toResponse(savedItem);
    }

    public TodoItemResponse toggle(Long id) {
        log.info("Переключение статуса выполнения для задачи с id: {}", id);
        try {
            TodoItem item = todoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Задача не найдена с id: {}", id);
                        return new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Todo item not found with id: " + id
                        );
                    });

            boolean newCompletedStatus = !item.isCompleted();
            log.debug("Переключение статуса с {} на {}", item.isCompleted(), newCompletedStatus);
            item.setCompleted(newCompletedStatus);

            TodoItem savedItem = todoRepository.save(item);

            log.info("Успешно переключена задача с id: {} на статус: {}", id, newCompletedStatus);
            return todoMapper.toResponse(savedItem);
        } catch (ResponseStatusException ex) {
            log.error("Ошибка при переключении задачи с id: {}, ошибка: {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Неожиданная ошибка при переключении задачи с id: {}, ошибка: {}", id, ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error toggling todo item with id: " + id
            );
        }
    }
}