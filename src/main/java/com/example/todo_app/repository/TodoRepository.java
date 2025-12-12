package com.example.todo_app.repository;

import com.example.todo_app.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TodoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TodoItem> todoItemRowMapper = (rs, rowNum) -> {
        TodoItem item = new TodoItem();
        item.setId(rs.getLong("id"));
        item.setTitle(rs.getString("title"));
        item.setDescription(rs.getString("description"));
        item.setDate(rs.getDate("date").toLocalDate());
        item.setCompleted(rs.getBoolean("completed"));
        return item;
    };

    public List<TodoItem> findAll() {
        String sql = "SELECT * FROM todo_items ORDER BY date DESC, id DESC";
        return jdbcTemplate.query(sql, todoItemRowMapper);
    }

    public List<TodoItem> findByDate(LocalDate date) {
        String sql = "SELECT * FROM todo_items WHERE date = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, todoItemRowMapper, date);
    }

    public Optional<TodoItem> findById(Long id) {
        String sql = "SELECT * FROM todo_items WHERE id = ?";
        try {
            TodoItem item = jdbcTemplate.queryForObject(sql, todoItemRowMapper, id);
            return Optional.ofNullable(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public TodoItem save(TodoItem todoItem) {
        String sql = "INSERT INTO todo_items (title, description, date, completed) VALUES (?, ?, ?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                todoItem.getTitle(),
                todoItem.getDescription(),
                todoItem.getDate(),
                todoItem.isCompleted()
        );
        todoItem.setId(id);
        return todoItem;
    }

    public void updateCompletedStatus(Long id, boolean completed) {
        String sql = "UPDATE todo_items SET completed = ? WHERE id = ?";
        jdbcTemplate.update(sql, completed, id);
    }
}