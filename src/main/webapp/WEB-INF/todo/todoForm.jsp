<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>새 할 일 추가</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <h2>새 할 일 추가</h2>
    <form action="add" method="post">
        <label for="title">제목:</label>
        <input type="text" id="title" name="title" required value="코딩1"><br>
        <label for="description">설명:</label>
        <textarea id="description" name="description" required>JSP 학습 1</textarea><br>
        <label for="dueDate">마감일:</label>
        <input type="date" id="dueDate" name="dueDate" required value="2024-06-30"><br>
        <label for="completed">완료 여부:</label>
        <input type="checkbox" id="completed" name="completed"><br>
        <button type="submit">추가</button>
    </form>
    <br>
    <a href="list">목록으로 돌아가기</a>
</body>
</html>
