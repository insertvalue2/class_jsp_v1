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
 * 이번 학습에 핵심 내용 
 * 
 * 회원과 관련된 요청을 처리하는 컨트롤러
 * MVC 패턴에서 컨트롤러의 주요 목적: 
 * 요청을 처리하고, 유효성 검사, 인증 검사 등을 수행하며, 
 * 필요한 경우 모델과 상호작용하고 뷰로 데이터를 전달한다. 
 */
@WebServlet("/user/*")
public class UserController extends HttpServlet {

    // 여러 사용자가 사용하는 인스턴스 (static으로 설계하면 안됨 - 전역 금지)
    private UserDAO userDAO = new UserDAOImpl();
    
    /**
     * 회원 가입 페이지 - http://localhost:8080/mvc/user/signUp
     * 로그인 페이지 - http://localhost:8080/mvc/user/signIn
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        System.out.println("GET 요청 action : " + action);
        switch (action) {
            case "/signIn":
                forwardToPage(request, response, "/WEB-INF/user/signIn.jsp");
                break;
            case "/signUp":
                forwardToPage(request, response, "/WEB-INF/user/signUp.jsp");
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

        // 방어적 코드 작성: 입력 값 유효성 검사
        if (isInvalid(username)) {
            setErrorMessageAndForward(request, response, "Username is required.", "/WEB-INF/user/signUp.jsp");
            return;
        }

        if (isInvalid(password)) {
            setErrorMessageAndForward(request, response, "Password is required.", "/WEB-INF/user/signUp.jsp");
            return;
        }

        if (isInvalid(email)) {
            setErrorMessageAndForward(request, response, "Email is required.", "/WEB-INF/user/signUp.jsp");
            return;
        }

        // 이메일 형식 유효성 검사
//        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//            setErrorMessageAndForward(request, response, "Invalid email format.", "/WEB-INF/user/signUp.jsp");
//            return;
//        }

        // UserDTO 객체 생성 및 데이터 설정
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // 디버깅을 위한 출력
        System.out.println(user.toString());
        // 사용자 추가
        userDAO.addUser(user);
        // 로그인 페이지로 리다이렉트
        response.sendRedirect("signIn");
    }

    /**
     * 로그인 처리
     * 로그인은 자원에 요청이지만 보안을 위해 예외 처리
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 데이터베이스에서 사용자 조회
        UserDTO user = userDAO.getUserByUsername(username);

        // 사용자 인증
        if (user != null && user.getPassword().equals(password)) {
            // 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("principal", user);
            
            // todo 페이지로 리다이렉트
            response.sendRedirect("../todo/form");
        } else {
            // 인증 실패 시 로그인 페이지로 리다이렉트
            response.sendRedirect("signIn?error=invalid");
        }
    }

    /**
     * 입력 값이 유효하지 않은지 확인하는 메서드
     * 
     * @param value
     * @return true if the value is null or empty
     */
    private boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 에러 메시지를 설정하고 지정된 페이지로 요청을 전달하는 메서드
     * 
     * @param request
     * @param response
     * @param errorMessage
     * @param page
     * @throws ServletException
     * @throws IOException
     */
    private void setErrorMessageAndForward(HttpServletRequest request, HttpServletResponse response, String errorMessage, String page) 
            throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher(page).forward(request, response);
    }

    /**
     * 지정된 페이지로 요청을 전달하는 메서드
     * 
     * @param request
     * @param response
     * @param page
     * @throws ServletException
     * @throws IOException
     */
    private void forwardToPage(HttpServletRequest request, HttpServletResponse response, String page) 
            throws ServletException, IOException {
        request.getRequestDispatcher(page).forward(request, response);
    }
}
