package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDao {
	
	/** HA-1. 프로필 이미지 조회 (p.2 / 메인페이지)
		input : member_id (회원 아이디)
		output : profile_img(프로필 이미지) */
	String getProfileImage(int memberId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";//어떤 데이터베이스?
		String url = "jdbc:oracle:thin:@localhost:1521:xe";//DB의 위치 (오라클의 위치)
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "SELECT profile_img FROM members WHERE member_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {			
			String profileImage = rs.getString("profile_img");
			rs.close();
			pstmt.close();
			conn.close();
			return profileImage;
		}else {
			rs.close();
			pstmt.close();
			conn.close();
			return null;
		}	
	}
	
	/** HA-4 이메일 조회 (p.3 / 로그인+간편가입 팝업, p.5 / 정보수정1)
	 	input : 이메일 
	 	output : false(결과없음), true(있음)*/
	boolean isExistEmail(String email) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";// 어떤 데이터베이스?
		String url = "jdbc:oracle:thin:@localhost:1521:xe";// DB의 위치 (오라클의 위치)
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT COUNT(*) FROM members WHERE email = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);

		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		rs.close();
		pstmt.close();
		conn.close();
		
		if(count>0) return true;
		return false;
	}
	
	/** HA-5 이메일, 비밀번호 조회 (p.3 / 로그인+간편가입 팝업, p.5 / 정보수정1)
		input : 이메일, 비밀번호  
		output : false(실패), true(성공)*/
	boolean canLogin(String email, String pw) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";// 어떤 데이터베이스?
		String url = "jdbc:oracle:thin:@localhost:1521:xe";// DB의 위치 (오라클의 위치)
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT COUNT(*) FROM members WHERE email = ? AND pw = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, pw);

		ResultSet rs = pstmt.executeQuery();
		rs.next();
		
		int count = rs.getInt(1);
		
		rs.close();
		pstmt.close();
		conn.close();
		
		if (count == 1)return true;
		return false;
	}

	/** HA-7 인증코드, 만료일시 조회 (p.5 / 정보수정1)
		input : 인증키  
		output : false(실패), true(성공)*/
	boolean isValidCode(String key) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";// 어떤 데이터베이스?
		String url = "jdbc:oracle:thin:@localhost:1521:xe";// DB의 위치 (오라클의 위치)
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT COUNT(*) FROM members WHERE key = ? AND ex_date > SYSDATE";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, key);

		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		rs.close();
		pstmt.close();
		conn.close();
		
		if (count==1)return true;
		return false;
	}

	/** HA-6 비밀번호 재설정 인증코드 수정 (p.5 / 정보수정1)
		input : 이메일
		output : - */
	void updateKey(String email) throws Exception {
		// 6자리 대문자, 소문자, 숫자로 이루어진 랜덤 key
		StringBuilder sb = new StringBuilder();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
            if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}

		String driver = "oracle.jdbc.driver.OracleDriver";// 어떤 데이터베이스?
		String url = "jdbc:oracle:thin:@localhost:1521:xe";// DB의 위치 (오라클의 위치)
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE members SET key =?, ex_date = SYSDATE+1/24/6 WHERE email = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, sb.toString());
		pstmt.setString(2, email);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	public static void main(String[] args) throws Exception {
		MemberDao a = new MemberDao();
		System.out.println();
		a.updateKey("cjsqldbtn@gmail.com");
	}

}
