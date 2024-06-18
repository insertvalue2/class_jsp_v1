package com.tenco.mvc.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBUtil {
	private static DataSource dataSource;

	static {
		try {
			// InitialContext 객체를 생성하여 JNDI 네임스페이스에 접근
			InitialContext ctx = new InitialContext();
			// JNDI 네임스페이스에서 "java:comp/env/jdbc/MyDB" 이름으로 데이터 소스를 찾음
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/MyDB");
		} catch (NamingException e) {
			// 네이밍 예외 발생 시 스택 트레이스를 출력
			e.printStackTrace();
		}
	}

	/**
	 * 데이터베이스 연결을 반환하는 메서드
	 * 
	 * @return 데이터베이스 연결 객체
	 * @throws SQLException SQL 예외 발생 시 던짐
	 */
	public static Connection getConnection() throws SQLException {
		// 데이터 소스를 통해 데이터베이스 연결을 반환
		return dataSource.getConnection();
	}
}
