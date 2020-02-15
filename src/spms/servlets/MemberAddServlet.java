package spms.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/member/add")
public class MemberAddServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/member/MemberForm.jsp");
		rd.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.setCharacterEncoding("UTF-8");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		//ResultSet rs = null;
		
		try {
			// 1. 사용할 JDBC 드라이버를 등록하라.
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//Class.forName(this.getInitParameter("driver"));
			
			ServletContext sc = this.getServletContext();
			// 2. 드라이버를 사용하여 MySQL 서버와 연결하라.
			conn = (Connection) sc.getAttribute("conn");
			
			// 3. 커넥션 객체로부터 SQL을 던질 객체를 준비하라.
			stmt = conn.prepareStatement(
					"INSERT INTO MEMBERS(EMAIL, PWD, MNAME, CRE_DATE, MOD_DATE)"
					+ " VAlUES (?, ?, ?, NOW(), NOW())"); // 입력 매개변수(In Parameter) 사용
			// 4. SQL을 던지는 객체를 사용하여 서버레 질의하라.
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("password"));
			stmt.setString(3, request.getParameter("name"));
			stmt.executeUpdate();
			
			response.sendRedirect("list"); // 6. 리다이렉트
			
			// 5. 서버에 가져온 데이터를 사용하여 HTML을 만들어서 웹 브라우저로 출력하라.
			// 6. 리프레시
			//response.setHeader("Refresh", "1;url=list");
			//response.addHeader("Refresh", "1;url=list");
		} catch (Exception e ) {
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		} finally {
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
			//try { if(conn != null) conn.close();} catch(Exception e) {}
		}
	}
}
