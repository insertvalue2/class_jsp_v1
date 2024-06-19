<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <h2>로그인</h2>
    <form action="signIn" method="post">
        <label for="username">사용자 이름:</label>
        <input type="text" id="username" name="username" required value="티모"><br>

        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required value="asd123"><br>

        <button type="submit">로그인</button>
    </form>
    <%
        String error = request.getParameter("error");
        if ("invalid".equals(error)) {
            out.println("<p style='color:red;'> 잘못된 요청 입니다. </p>");
        }
    %>
</body>
</html>
