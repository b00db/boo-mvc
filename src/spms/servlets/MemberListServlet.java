package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.vo.Member;

@SuppressWarnings("serial")
@WebServlet("/member/list")
public class MemberListServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			// 1. 사용할 JDBC 드라이버를 등록하라.
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//ServletConfig config = this.getServletConfig();
			//Class.forName(config.getInitParameter("driver"));
			//Class.forName(this.getInitParameter("driver"));
			
			ServletContext sc = this.getServletContext();
			//Class.forName(sc.getInitParameter("driver"));
			
			// 2. 드라이버를 사용하여 MySQL 서버와 연결하라.
			/*conn = DriverManager.getConnection(
					sc.getInitParameter("url"),
					sc.getInitParameter("username"),
					sc.getInitParameter("password")); // url, id, pwd*/
			// 3. 커넥션 객체로부터 SQL을 던질 객체를 준비하라.
			conn = (Connection) sc.getAttribute("conn");
			stmt = conn.createStatement();
			// 4. SQL을 던지는 객체를 사용하여 서버에 질의하라!
			rs = stmt.executeQuery(
				"select MNO, MNAME, EMAIL, CRE_DATE" +
				" from MEMBERS" +
				" order by MNO ASC");
			
			// 5. 서버에 가져온 데이터를 사용하여 HTML만들어서 웹 브라우저로 출력하라.
			response.setContentType("text/html; charset=UTF-8");
			ArrayList<Member> members = new ArrayList<Member>();
			
			// 데이터베이스에서 회원 정보를 가져와 Member에 담는다.
			// 그리고 Member객체를 ArrayList에 추가한다.
			while(rs.next()) {
				members.add(new Member()
						.setNo(rs.getInt("MNO"))
						.setName(rs.getString("MNAME"))
						.setEmail(rs.getString("EMAIL"))
						.setCreatedDate(rs.getDate("CRE_DATE"))		);
			}
			
			// request에 회원 목록 데이터 보관한다.
			request.setAttribute("members", members);
			
			// JSP로 출력을 위임한다.
			RequestDispatcher rd = request.getRequestDispatcher("/member/MemberList.jsp");
			rd.include(request, response);
			
		} catch(Exception e) {
			//throw new ServletException(e);
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		} finally {
			try {rs.close();} catch (Exception e) {}
			try {stmt.close();} catch (Exception e) {}
			//try {conn.close();} catch (Exception e) {}
		}
	}
	
}