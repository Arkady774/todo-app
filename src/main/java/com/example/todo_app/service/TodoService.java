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

    // Дополнительные методы для работы с БД:

    public void update(TodoItem todoItem) {
        String sql = "UPDATE todo_items SET title = ?, description = ?, date = ?, completed = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                todoItem.getTitle(),
                todoItem.getDescription(),
                java.sql.Date.valueOf(todoItem.getDate()),
                todoItem.isCompleted(),
                todoItem.getId()
        );
    }

    public void delete(Long id) {
        String sql = "DELETE FROM todo_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<TodoItem> getCompleted() {
        String sql = "SELECT * FROM todo_items WHERE completed = true ORDER BY date DESC";
        return jdbcTemplate.query(sql, todoItemRowMapper);
    }

    public List<TodoItem> getPending() {
        String sql = "SELECT * FROM todo_items WHERE completed = false ORDER BY date DESC";
        return jdbcTemplate.query(sql, todoItemRowMapper);
    }
}