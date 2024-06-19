package com.tenco.mvc.controller;

import java.io.IOException;

import com.tenco.mvc.models.UserDAO;
import com.tenco.mvc.models.UserDAOImpl;
import com.tenco.mvc.models.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 회원과 관련된 요청이 들어 올 경우 처리 MVC 패턴에서 컨트롤러의 주요 목적이 뭘까?
 */
// http://localhost:8080/user/xxx 로 들어오는 요청 처리 
@WebServlet("/user/*")
public class UserController extends HttpServlet {

	// 여러 사용자가 사용하는 인스턴스 (static 으로 설계 하면 안됨 - 전역 금지 x)
	private UserDAO userDAO = new UserDAOImpl();
	
	/**
	 * 회원 가입 페이지 - http://localhost:8080/mvc/user/signUp
	 * 로그인 페이지 - http://localhost:8080/mvc/user/singIn
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		System.out.println("GET 요청 action : " + action);
		switch (action) {
		case "/signIn":
			request.getRequestDispatcher("/WEB-INF/user/signIn.jsp").forward(request, response);
			break;
		case "/signUp":
			request.getRequestDispatcher("/WEB-INF/user/signUp.jsp").forward(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getPathInfo();
		System.out.println("POST 요청 action : " + action);
		
		switch (action) {
		case "/signUp":
			signUp(request, response);
			break;
		case "/signIn":
			signIn(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	/**
	 * 회원 가입 처리
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		
		// 방어적 코드 작성
		if (username == null || username.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Username is required.");
			request.getRequestDispatcher("/WEB-INF/user/signUp.jsp").forward(request, response);
			return;
		}
		// getRequestDispatcher란?
		// 다른 자원(JSP 페이지, 서블릿 등)으로 요청을 전달하거나 포함하기 위해 사용됩니다.
		// forward 메서드
		// 클라이언트로부터 받은 요청을 지정된 자원으로 내부 전달하고, 
		// 클라이언트는 요청이 다른 자원으로 전달되었는지 알 수 없음.
		if (password == null || password.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Password is required.");
			request.getRequestDispatcher("/WEB-INF/user/signUp.jsp").forward(request, response);
			return;
		}

		if (email == null || email.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Email is required.");
			request.getRequestDispatcher("/WEB-INF/user/signUp.jsp").forward(request, response);
			return;
		}
		// 이메일 형식 (정규 표현식 사용 가능) 
		
		UserDTO user = new UserDTO();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		
		System.out.println(user.toString());
		userDAO.addUser(user);
		// URL 요청 이다. (내부 이동 아님, 확장자 x) - 상대 경로 설정  
		response.sendRedirect("signIn");
	}

	/**
	 * 로그인 처리 로그인은 자원에 요청이지만 예외 (보안)
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * 
	 */
	private void signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		UserDTO user = userDAO.getUserByUsername(username);

		if (user != null && user.getPassword().equals(password)) {
			HttpSession session = request.getSession();
			// 시스템에 접근하는 주체를 나타내는 개념으로, 
			// 보안 컨텍스트에서 인증된 사용자나 서비스 계정을 의미한다.
			session.setAttribute("principal", user);
			// todo 만들 예정  
			response.sendRedirect("../todo/form");
		} else {
			// 새로은 요청 URL 로 처리 (클라이언트로 가서 새로운 req,res 생성 된다) 
			response.sendRedirect("signIn?error=invalid");
		}
	}
}