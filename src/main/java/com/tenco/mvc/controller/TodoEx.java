package com.tenco.mvc.controller;

import java.io.IOException;
import java.util.List;

import com.tenco.mvc.models.TodoDAO;
import com.tenco.mvc.models.TodoDAOImpl;
import com.tenco.mvc.models.TodoDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/todoEx/*")
public class TodoEx extends HttpServlet {

    private TodoDAO todoDAO;

    @Override
    public void init() throws ServletException {
        this.todoDAO = new TodoDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();

        switch (action) {
            case "/add":
                addTodo(request, response);
                break;
            case "/update":
                updateTodo(request, response);
                break;
            case "/delete":
                deleteTodo(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void addTodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDateStr = request.getParameter("dueDate");
        boolean completed = Boolean.parseBoolean(request.getParameter("completed"));

        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");

        TodoDTO todo = new TodoDTO();
        todo.setUserId(userId);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDueDate(java.sql.Date.valueOf(dueDateStr));
        todo.setCompleted(completed);

        todoDAO.addTodo(todo);
        response.sendRedirect("todoList.jsp");
    }

    private void updateTodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDateStr = request.getParameter("dueDate");
        boolean completed = Boolean.parseBoolean(request.getParameter("completed"));

        TodoDTO todo = new TodoDTO();
        todo.setId(id);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDueDate(java.sql.Date.valueOf(dueDateStr));
        todo.setCompleted(completed);

        //todoDAO.updateTodo(todo);
        response.sendRedirect("todoList.jsp");
    }

    private void deleteTodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        //todoDAO.deleteTodo(id);
        response.sendRedirect("todoList.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();

        switch (action) {
            case "/list":
                listTodos(request, response);
                break;
            case "/detail":
                getTodoDetail(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void listTodos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");

        List<TodoDTO> todos = todoDAO.getTodosByUserId(userId);
        request.setAttribute("todos", todos);
        request.getRequestDispatcher("/jsp/todoList.jsp").forward(request, response);
    }

    private void getTodoDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        TodoDTO todo = todoDAO.getTodoById(id);
        request.setAttribute("todo", todo);
        request.getRequestDispatcher("/jsp/todoDetail.jsp").forward(request, response);
    }
}