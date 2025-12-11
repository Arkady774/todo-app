package com.example.todo_app.service;

import com.example.todo_app.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

//TODO перенести всю логику работы с SQl в TodoRepository. Сервис же в свою очередь будет использовать методы репозитория.
@Service
public class TodoService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TodoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper для преобразования ResultSet в TodoItem
    private final RowMapper<TodoItem> todoItemRowMapper = (rs, rowNum) -> {
        TodoItem item = new TodoItem();
        item.setId(rs.getLong("id"));
        item.setTitle(rs.getString("title"));
        item.setDescription(rs.getString("description"));
        item.setDate(rs.getDate("date").toLocalDate());
        item.setCompleted(rs.getBoolean("completed"));
        return item;
    };

    public List<TodoItem> getAll() {
        String sql = "SELECT * FROM todo_items ORDER BY date DESC, id DESC";
        return jdbcTemplate.query(sql, todoItemRowMapper);
    }

    public List<TodoItem> getByDate(LocalDate date) {
        String sql = "SELECT * FROM todo_items WHERE date = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, todoItemRowMapper, date);
    }

    public TodoItem getById(Long id) {
        String sql = "SELECT * FROM todo_items WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, todoItemRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Todo item not found with id: " + id
            );
        }
    }

    public TodoItem create(TodoItem todoItem) {
        String sql = "INSERT INTO todo_items (title, description, date, completed) VALUES (?, ?, ?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class,  todoItem.getTitle(), todoItem.getDescription(), todoItem.getDate(), false);
        todoItem.setId(id);

        return todoItem;
    }

    public TodoItem toggle(Long id) {
        // Сначала получаем текущее состояние
        TodoItem item = getById(id);

        // Обновляем поле completed
        String sql = "UPDATE todo_items SET completed = ? WHERE id = ?";
        jdbcTemplate.update(sql, !item.isCompleted(), id);

        // Возвращаем обновленный элемент
        item.setCompleted(!item.isCompleted());
        return item;
    }
}