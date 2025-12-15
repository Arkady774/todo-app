package com.example.todo_app.service;

import com.example.todo_app.model.TodoItem;
import com.example.todo_app.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
        log.info("TodoService инициализирован с репозиторием");
    }

    public List<TodoItem> getAll() {
        log.info("Получение всех задач");
        List<TodoItem> items = todoRepository.findAll();
        log.info("Получено {} задач", items.size());
        return items;
    }

    public List<TodoItem> getByDate(LocalDate date) {
        log.info("Получение задач на дату: {}", date);
        List<TodoItem> items = todoRepository.findByDate(date);
        log.info("Получено {} задач на дату: {}", items.size(), date);
        return items;
    }

    public TodoItem getById(Long id) {
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
            return item;
        } catch (ResponseStatusException ex) {
            log.error("Не удалось получить задачу с id: {}, ошибка: {}", id, ex.getMessage());
            throw ex;
        }
    }

    public TodoItem create(TodoItem todoItem) {
        log.info("Создание новой задачи с заголовком: {}", todoItem.getTitle());
        todoItem.setCompleted(false);
        TodoItem savedItem = todoRepository.save(todoItem);
        log.info("Задача успешно создана с id: {}", savedItem.getId());
        return savedItem;
    }

    public TodoItem toggle(Long id) {
        log.info("Переключение статуса выполнения для задачи с id: {}", id);
        try {
            TodoItem item = getById(id);

            boolean newCompletedStatus = !item.isCompleted();
            log.debug("Переключение статуса с {} на {}", item.isCompleted(), newCompletedStatus);

            todoRepository.updateCompletedStatus(id, newCompletedStatus);
            item.setCompleted(newCompletedStatus);

            log.info("Успешно переключена задача с id: {} на статус: {}", id, newCompletedStatus);
            return item;
        } catch (Exception ex) {
            log.error("Ошибка при переключении задачи с id: {}, ошибка: {}", id, ex.getMessage());
            throw ex;
        }
    }
}