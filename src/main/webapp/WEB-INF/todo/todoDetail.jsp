<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tenco.mvc.models.TodoDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>할 일 상세 정보</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <h2>할 일 상세 정보</h2>
    <%
        TodoDTO todo = (TodoDTO) request.getAttribute("todo");
        if (todo != null) {
    %>
        <p>제목: <%= todo.getTitle() %></p>
        <p>설명: <%= todo.getDescription() %></p>
        <p>마감일: <%= todo.getDueDate() %></p>
        <p>완료 여부: <%= todo.isCompleted() ? "완료" : "미완료" %></p>
        <br>
        <hr>
        <br>
        <form action="update" method="post">
            <input type="hidden" name="id" value="<%= todo.getId() %>">
            <label for="title">제목:</label>
            <input type="text" id="title" name="title" value="<%= todo.getTitle() %>" required><br>
            <label for="description">설명:</label>
            <textarea id="description" name="description" required><%= todo.getDescription() %></textarea><br>
            <label for="dueDate">마감일:</label>
            <input type="date" id="dueDate" name="dueDate" value="<%= todo.getDueDate() %>" required><br>
            <label for="completed">완료 여부:</label>
            <input type="checkbox" id="completed" name="completed" <%= todo.isCompleted() ? "checked" : "" %>><br>
            <button type="submit">수정</button>
        </form>
    <%
        } else {
            out.println("<p>할 일 정보를 가져오는 데 실패했습니다.</p>");
        }
    %>
    <br>
    <a href="list">목록으로 돌아가기</a>
</body>
</html>
