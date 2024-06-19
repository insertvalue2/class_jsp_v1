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

	private TodoDAO todoDAO;

	/**
	 * 서블릿이 초기화될 때 한 번 실행됨. 여기서 TodoDAO 인스턴스를 초기화.
	 */
	@Override
	public void init() throws ServletException {
		this.todoDAO = new TodoDAOImpl();
	}

	/**
	 * GET 요청을 처리하는 메서드.
	 * 
	 * @param request  HttpServletRequest 객체.
	 * @param response HttpServletResponse 객체.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		System.out.println("GET todo : " + action);

		switch (action) {
		case "/form":
			todoFormPage(request, response);
			break;
		case "/list":
			todoListPage(request, response);
			break;
		case "/detail":
			getTodoDetail(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	/**
	 * 특정 할 일의 세부 정보를 가져오는 메서드.
	 * 
	 * @param request  HttpServletRequest 객체.
	 * @param response HttpServletResponse 객체.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getTodoDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		TodoDTO todo = todoDAO.getTodoById(id);
		request.setAttribute("todo", todo);
		request.getRequestDispatcher("/WEB-INF/todo/todoDetail.jsp").forward(request, response);
	}

	/**
	 * 특정 할 일을 삭제하는 메서드.
	 * 
	 * @param request  HttpServletRequest 객체.
	 * @param response HttpServletResponse 객체.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteTodo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");
		// 인증 검사
		if (principal == null) {
			response.sendRedirect(request.getContextPath() + "/user/signIn?error=invalid");
			return;
		}
		int todoId = Integer.parseInt(request.getParameter("id"));
		todoDAO.deleteTodo(principal.getId(), todoId);
		System.out.println("todoID : " + todoId);
		response.sendRedirect("list");
	}

	/**
	 * 할 일 목록 페이지로 이동하는 메서드.
	 * 
	 * @param request  HttpServletRequest 객체.
	 * @param response HttpServletResponse 객체.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void todoListPage(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
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
		// todoList.jsp 페이지로 요청 전달
		request.getRequestDispatcher("/WEB-INF/todo/todoList.jsp").forward(request, response);
	}

	/**
	 * 할 일 작성 페이지로 이동하는 메서드.
	 * 
	 * @param request  HttpServletRequest 객체.
	 * @param response HttpServletResponse 객체.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void todoFormPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 세션에서 principal 객체를 가져옴
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");

		// 현재 웹 애플리케이션의 컨텍스트 경로(Context Path)를 반환, 즉 context root를 의미.
		System.out.println("getContextPath : " + request.getContextPath());
		// 인증 검사
		if (principal == null) {
			response.sendRedirect(request.getContextPath() + "/user/signIn?error=invalid");
			return;
		}
		// todoForm.jsp 페이지로 요청 전달
		request.getRequestDispatcher("/WEB-INF/todo/todoForm.jsp").forward(request, response);
	}

	/**
	 * POST 요청을 처리하는 메서드.
	 * 
	 * @param request  HttpServletRequest 객체.
	 * @param response HttpServletResponse 객체.
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 인증 검사
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("principal");
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/user/signIn");
			return;
		}
		String action = request.getPathInfo();
		switch (action) {
		case "/add":
			addTodo(request, response);
			break;
		case "/delete":
			deleteTodo(request, response);
			break;
		case "/update":
			updateTodo(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	/**
	 * 할 일을 업데이트하는 메서드.
	 * 
	 * @param request  HttpServletRequest 객체.
	 * @param response HttpServletResponse 객체.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void updateTodo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String dueDateStr = request.getParameter("dueDate");
		boolean completed = "on".equalsIgnoreCase(request.getParameter("completed"));

		TodoDTO todo = new TodoDTO();
		todo.setId(id);
		todo.setTitle(title);
		todo.setDescription(description);
		todo.setDueDate(java.sql.Date.valueOf(dueDateStr));
		todo.setCompleted(completed);

		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");

		todoDAO.updateTodo(principal.getId(), todo);
		response.sendRedirect("list");
	}

	/**
	 * 새로운 할 일을 추가하는 메서드.
	 * 
	 * @param request  HttpServletRequest 객체.
	 * @param response HttpServletResponse 객체.
	 * @throws IOException
	 */
	private void addTodo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// HTTP 요청 메시지 Body에서 값 추출
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String dueDateStr = request.getParameter("dueDate");

		// 체크박스 값 받아내기 - 체크박스가 선택되지 않으면 null을 반환하고 체크가 되어 있다면 "on" 문자열을 반환함.
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
