package com.tenco.mvc.models;

import java.util.List;

public interface TodoDAO {
    void addTodo(TodoDTO todo);
    TodoDTO getTodoById(int id);
    List<TodoDTO> getTodosByUserId(int userId);
    List<TodoDTO> getAllTodos();
    void updateTodo(int principalId, TodoDTO todo);
    void deleteTodo(int principalId, int id);
}
