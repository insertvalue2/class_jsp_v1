<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <h2>회원가입</h2>
    <!-- 에러 메시지 출력 -->
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
        <p style="color: red;"><%= errorMessage %></p>
    <%
        }
    %>

    <!-- 상대 경로 VS 절대 경로  -->
    <!-- 상대 경로는 현재 페이지의 위치를 기준으로 경로를 지정합니다 -->
    <form action="/mvc/user/signUp" method="post">
        <label for="username">사용자 이름:</label>
        <input type="text" id="username" name="username" value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"><br>

        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" ><br>

        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"><br>

        <button type="submit">회원가입</button>
    </form>
</body>
</html>
