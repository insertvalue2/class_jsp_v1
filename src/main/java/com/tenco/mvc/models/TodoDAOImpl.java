package com.tenco.mvc.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class TodoDAOImpl implements TodoDAO {

	private DataSource dataSource;

	public TodoDAOImpl() {
		try {
			InitialContext ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/MyDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addTodo(TodoDTO todo) {
		String sql = "INSERT INTO todos (user_id, title, description, due_date, completed) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, todo.getUserId());
			ps.setString(2, todo.getTitle());
			ps.setString(3, todo.getDescription());
			ps.setDate(4, new java.sql.Date(todo.getDueDate().getTime()));
			ps.setBoolean(5, todo.isCompleted());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public TodoDTO getTodoById(int id) {
		String sql = "SELECT * FROM todos WHERE id = ?";
		TodoDTO todo = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					todo = new TodoDTO();
					todo.setId(rs.getInt("id"));
					todo.setUserId(rs.getInt("user_id"));
					todo.setTitle(rs.getString("title"));
					todo.setDescription(rs.getString("description"));
					todo.setDueDate(rs.getDate("due_date"));
					todo.setCompleted(rs.getBoolean("completed"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return todo;
	}

	@Override
	public List<TodoDTO> getTodosByUserId(int userId) {
		String sql = "SELECT * FROM todos WHERE user_id = ?";
		List<TodoDTO> todos = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					TodoDTO todo = new TodoDTO();
					todo.setId(rs.getInt("id"));
					todo.setUserId(rs.getInt("user_id"));
					todo.setTitle(rs.getString("title"));
					todo.setDescription(rs.getString("description"));
					todo.setDueDate(rs.getDate("due_date"));
					todo.setCompleted(rs.getBoolean("completed"));
					todos.add(todo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return todos;
	}

	@Override
	public List<TodoDTO> getAllTodos() {
		String sql = "SELECT * FROM todos";
		List<TodoDTO> todos = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				TodoDTO todo = new TodoDTO();
				todo.setId(rs.getInt("id"));
				todo.setUserId(rs.getInt("user_id"));
				todo.setTitle(rs.getString("title"));
				todo.setDescription(rs.getString("description"));
				todo.setDueDate(rs.getDate("due_date"));
				todo.setCompleted(rs.getBoolean("completed"));
				todos.add(todo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return todos;
	}

	@Override
	public void updateTodo(int principalId, TodoDTO todo) {
		int userId = principalId;
		String sql = "UPDATE todos SET title = ?, description = ?, due_date = ?, completed = ? WHERE id = ? AND user_id = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, todo.getTitle());
			ps.setString(2, todo.getDescription());
			ps.setDate(3, new java.sql.Date(todo.getDueDate().getTime()));
			ps.setBoolean(4, todo.isCompleted());
			ps.setInt(5, todo.getId());
			ps.setInt(6, userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteTodo(int principalId, int id) {
	
		int userId = principalId;
		String sql = "DELETE FROM todos WHERE id = ? AND user_id = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.setInt(2, userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
