package com.tenco.mvc.controller;

import java.io.IOException;
import java.util.List;

import com.tenco.mvc.models.TodoDAO;
import com.tenco.mvc.models.TodoDAOImpl;
import com.tenco.mvc.models.TodoDTO;
import com.tenco.mvc.models.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/todo/*")
public class TodoController extends HttpServlet {

	// 코드 추가
	private TodoDAO todoDAO;

	/**
	 * 생성될 때 최조 한번 수행 init()
	 */
	@Override
	public void init() throws ServletException {
		this.todoDAO = new TodoDAOImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		System.out.println("GET todo : " + action);
		
		switch (action) {
		case "/form":
			todoFromPage(request, response);
			break;
		case "/list":
			todoListPage(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	private void todoListPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 세션에서 principal 객체를 가져옴
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");
		// 인증 검사
		if (principal == null) {
			response.sendRedirect(request.getContextPath() + "/user/signIn?error=invalid");
			return;
		}
		
		List<TodoDTO> todos = todoDAO.getTodosByUserId(principal.getId());
		request.setAttribute("todos", todos);
		// todoForm.jsp 페이지로 요청 전달
		request.getRequestDispatcher("/WEB-INF/todo/todoList.jsp").forward(request, response);
	}

	private void todoFromPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 세션에서 principal 객체를 가져옴
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");

		// 현재 웹 애플리케이션의 컨텍스트 경로(Context Path)를 반환, 즉 context root 를 의미 한다.
		System.out.println("getContextPath : " + request.getContextPath());
		// 인증 검사
		if (principal == null) {
			response.sendRedirect(request.getContextPath() + "/user/signIn?error=invalid");
			return;
		}
		// todoForm.jsp 페이지로 요청 전달
		request.getRequestDispatcher("/WEB-INF/todo/todoForm.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 인증 검사
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("principal");
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/user/singIn");
			return;
		}
		String action = request.getPathInfo();
		switch (action) {
		case "/add":
			addTodo(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	private void addTodo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 인증검사 먼저일까 유효성 먼저 검사 일까?

		// HTTP 요청 메세지 Body 에서 값 추출
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String dueDateStr = request.getParameter("dueDate");
		
		// checkBox 값 받아내기 - 실수 하는 부분들  
		// 체크박스가 선택되지 않으면 null 을 반환하고 체크가 되어 있다면 on 문자열을 반환 한다.  
		boolean completed = "on".equalsIgnoreCase(request.getParameter("completed"));
		System.out.println("completed " + completed);
		
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");

		TodoDTO todo = new TodoDTO();
		todo.setUserId(principal.getId());
		todo.setTitle(title);
		todo.setDescription(description);
		todo.setDueDate(java.sql.Date.valueOf(dueDateStr));
		todo.setCompleted(completed);

		todoDAO.addTodo(todo);
		response.sendRedirect(request.getContextPath() + "/todo/list");
	}

}