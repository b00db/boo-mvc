package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/delete")
@SuppressWarnings("serial")
public class MemberDeleteServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.setCharacterEncoding("UTF-8");
		Connection conn = null;
		Statement stmt = null;

		try {
			// 1. 사용할 JDBC 드라이버를 등록하라.
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));

			// 2. 드라이버를 사용하여 MySQL 서버와 연결하라.
			conn = DriverManager.getConnection(
					sc.getInitParameter("url"),
					sc.getInitParameter("username"),
					sc.getInitParameter("password")); // url, id, pwd

			// 3. 커넥션 객체로부터 SQL을 던질 객체를 준비하라.
			stmt = conn.createStatement();
			// 4. SQL을 던지는 객체를 사용하여 서버레 질의하라.
			stmt.executeUpdate(
					"DELETE FROM MEMBERS WHERE MNO=" + 
							request.getParameter("no"));
			
			response.sendRedirect("list"); // 6. 리다이렉트

		} catch (Exception e ) {
			throw new ServletException(e);
		} finally {
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
			try { if(conn != null) conn.close();} catch(Exception e) {}
		}
	}
}
