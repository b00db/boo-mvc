package spms.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			// 1. 사용할 JDBC 드라이버를 등록하라.
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//Class.forName(this.getInitParameter("driver"));
			
			ServletContext ctx = this.getServletContext();
			Class.forName(ctx.getInitParameter("driver"));
			
			// 2. 드라이버를 사용하여 MySQL 서버와 연결하라.
			conn = DriverManager.getConnection(
					ctx.getInitParameter("url"),
					ctx.getInitParameter("username"),
					ctx.getInitParameter("password")); // url, id, pwd
			
			// 3. 커넥션 객체로부터 SQL을 던질 객체를 준비하라.
			stmt = conn.createStatement();
			
			// 4. SQL을 던지는 객체를 사용하여 서버에 질의하라!
			rs = stmt.executeQuery(
				"select MNO, MNAME, EMAIL, CRE_DATE from MEMBERS" +
				" where MNO=" + request.getParameter("no"));
			rs.next();
			
			// 5. 서버에 가져온 데이터를 사용하여 HTML을 만들어서 웹 브라우저로 출력하라.
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>회원정보</title></head>");
			out.println("<body><h1>회원정보</h1>");
			out.println("<form action='update' method='post'>");
			out.println("번호: <input type='text' name='no' value='" + request.getParameter("no") + "') readonly><br>");
			out.println("이름: *<input type='text' name='name'" + " value='" + rs.getString("MNAME") + "'><br>");
			out.println("이메일: <input type='text' name='email'" + " value='" + rs.getString("EMAIL") + "'><br>");
			out.println("가입일: " + rs.getDate("CRE_DATE") + "<br>");
			out.println("<input type='submit' value='저장'>");
			out.println("<input type='button' value='삭제' "
					+ "onclick='location.href=\"delete?no=" + 
					request.getParameter("no") + "\";'>");
			out.println("<input type='button' value='취소'" + " onclick='location.href=\"list\"'>");
			out.println("</form>");
			out.println("</body></html>");

		} catch (Exception e ) {
			throw new ServletException(e);
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) {}
			try { if(stmt != null) stmt.close(); } catch (Exception e) {}
			try { if(conn != null) conn.close(); } catch (Exception e) {}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.setCharacterEncoding("UTF-8");
		Connection conn = null;
		PreparedStatement stmt = null;
		
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
			stmt = conn.prepareStatement(
					"UPDATE MEMBERS SET EMAIL=?, MNAME=?, MOD_DATE=now()"
					+ " WHERE MNO=?"); // 입력 매개변수(In Parameter) 사용
			// 4. SQL을 던지는 객체를 사용하여 서버레 질의하라.
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("name"));
			stmt.setInt(3, Integer.parseInt(request.getParameter("no")));
			stmt.executeUpdate();
			
			response.sendRedirect("list"); // 6. 리다이렉트
			
		} catch (Exception e ) {
			throw new ServletException(e);
		} finally {
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
			try { if(conn != null) conn.close();} catch(Exception e) {}
		}
	}
}
