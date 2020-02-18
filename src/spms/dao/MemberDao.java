package spms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import spms.vo.Member;

public class MemberDao {
	Connection connection;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	// 회원 정보 조회
	public List<Member> selectList() throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = connection.createStatement();
			// SQL을 던지는 객체를 사용하여 서버에 질의하라!
			rs = stmt.executeQuery(
				"SELECT MNO, MNAME, EMAIL, CRE_DATE" +
				" FROM MEMBERS" +
				" ORDER BY MNO ASC");
			
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
			
			return members;

		} catch(Exception e) {
			throw e;
			
		} finally {
			try {if (rs != null) rs.close();} catch (Exception e) {}
			try {if (stmt != null) stmt.close();} catch (Exception e) {}
		}
	}
	
	// 회원 등록
	public int insert(Member member) throws Exception {
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(
					"INSERT INTO MEMBERS(EMAIL, PWD, MNAME, CRE_DATE, MOD_DATE)"
					+ " VAlUES (?, ?, ?, NOW(), NOW())"); // 입력 매개변수(In Parameter) 사용
			stmt.setString(1, member.getEmail());
			stmt.setString(2, member.getPassword());
			stmt.setString(3, member.getName());
			
			return stmt.executeUpdate();
			
		} catch (Exception e) {
		      throw e;
			
		} finally {
		    try {if (stmt != null) stmt.close();} catch(Exception e) {}
	    }
	}
	
	// 회원 삭제
	public int delete(int no) throws Exception {
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			return stmt.executeUpdate(
					"DELETE FROM MEMBERS WHERE MNO=" + no);

		} catch (Exception e ) {
			throw e;
			
		} finally {
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
		}
	}
	
	// 회원 상세 정보 조회
	public Member selectOne(int no) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = connection.createStatement();
			
			 rs = stmt.executeQuery(
			      "SELECT MNO,EMAIL,MNAME,CRE_DATE FROM MEMBERS" + 
			      " WHERE MNO=" + no);   
			 
			 if (rs.next()) {
				 return new Member()
			     .setNo(rs.getInt("MNO"))
			     .setEmail(rs.getString("EMAIL"))
			     .setName(rs.getString("MNAME"))
			     .setCreatedDate(rs.getDate("CRE_DATE"));
			        
			} else {
				throw new Exception("해당 번호의 회원을 찾을 수 없습니다.");
			}
			
		} catch (Exception e ) {
			throw e;
			
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) {}
			try { if(stmt != null) stmt.close(); } catch (Exception e) {}
		}
	}
	
	// 회원 정보 변경
	public int update(Member member) throws Exception {
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(
					"UPDATE MEMBERS SET EMAIL=?, MNAME=?, MOD_DATE=now()"
					+ " WHERE MNO=?"); // 입력 매개변수(In Parameter) 사용
			stmt.setString(1, member.getEmail());
			stmt.setString(2, member.getName());
			stmt.setInt(3, member.getNo());
			
			return stmt.executeUpdate();
			
		} catch (Exception e ) {
			throw e;
			
		} finally {
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
		}
	}
	
	// 로그인
	// 있으면 Member 객체 리턴, 없으면 null 리턴
	public Member exist(String email, String password) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = connection.prepareStatement(
							"SELECT MNAME, EMAIL FROM MEMBERS"
							+ " WHERE EMAIL=? AND PWD=?");
			stmt.setString(1, email);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if (rs.next()) {
		        return new Member()
		          .setName(rs.getString("MNAME"))
		          .setEmail(rs.getString("EMAIL"));
		      } else {
		        return null;
		      }
	
		} catch (Exception e) {
			throw e;
			
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		}
	}
}
