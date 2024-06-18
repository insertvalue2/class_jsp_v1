package com.tenco.mvc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasicDBUtil {
	
	private static final String URL = "jdbc:mysql://localhost:3306/tb_todo?serverTimezone=Asia/Seoul";
	private static final String USER = "root";
	private static final String PASSWORD = "asd123";

	/**
	 * 데이터베이스 연결을 반환하는 메서드
	 * 
	 * @return 데이터베이스 연결 객체
	 * @throws SQLException SQL 예외 발생 시 던짐
	 */
	public static Connection getConnection() throws SQLException {
		// JDBC 드라이버 로드
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new SQLException("MySQL JDBC 드라이버를 로드할 수 없습니다.", e);
		}
		// 데이터베이스 연결 반환
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
