<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tenco.mvc.models.TodoDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>할 일 목록</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <h2>할 일 목록</h2>
    <a href="form">새 할 일 추가</a>
    <%
        List<TodoDTO> todos = (List<TodoDTO>) request.getAttribute("todos");
        if (todos != null && !todos.isEmpty()) {
    %>
        <table border="1">
            <tr>
                <th>제목</th>
                <th>설명</th>
                <th>마감일</th>
                <th>완료 여부</th>
                <th>액션</th>
            </tr>
            <%
                for (TodoDTO todo : todos) {
            %>
            <tr>
                <td><%= todo.getTitle() %></td>
                <td><%= todo.getDescription() %></td>
                <td><%= todo.getDueDate() %></td>
                <td><%= todo.isCompleted() ? "완료" : "미완료" %></td>
                <td>
                    <a href="todo/detail?id=<%= todo.getId() %>">상세 보기</a>
                    <form action="todo/delete" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="<%= todo.getId() %>">
                        <button type="submit">삭제</button>
                    </form>
                </td>
            </tr>
            <%
                }
            %>
        </table>
    <%
        } else {
            out.println("<p>등록된 할 일이 없습니다.</p>");
        }
    %>
</body>
</html>
