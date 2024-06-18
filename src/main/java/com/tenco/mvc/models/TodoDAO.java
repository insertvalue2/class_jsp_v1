package com.tenco.mvc.models;

import java.util.List;

public interface TodoDAO {
    void addTodo(TodoDTO todo);
    TodoDTO getTodoById(int id);
    List<TodoDTO> getTodosByUserId(int userId);
    List<TodoDTO> getAllTodos();
    void updateTodo(TodoDTO todo);
    void deleteTodo(int id);
}
