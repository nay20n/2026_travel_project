package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDao {
	
	/** HA-1. 프로필 이미지 조회 (p.2 / 메인페이지)
		input : member_id (회원 아이디)
		output : profileImg(프로필 이미지) */
	String getProfileImage(int memberId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "SELECT profile_img FROM members WHERE member_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {			
			String profileImg = rs.getString("profile_img");
			rs.close();
			pstmt.close();
			conn.close();
			return profileImg;
		}else {
			rs.close();
			pstmt.close();
			conn.close();
			return null;
		}	
	}
	
	/** HA-4 이메일 조회 (p.3 / 로그인+간편가입 팝업, p.5 / 정보수정1)
	 	input : 이메일(email)
	 	output : false(결과없음), true(있음)*/
	boolean isExistEmail(String email) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
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
		input : 이메일(email), 비밀번호(pw)  
		output : false(실패), true(성공)*/
	boolean canLogin(String email, String pw) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
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

	/** HA-6 비밀번호 재설정 인증코드 수정 (p.5 / 정보수정1)
		input : 이메일(email)
		output : - */
	void updateKey(String email) throws Exception {
		// 6자리 대문자, 소문자, 숫자로 이루어진 랜덤 key
		StringBuilder sb = new StringBuilder();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
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
	
	/** HA-7 인증코드, 만료일시 조회 (p.5 / 정보수정1)
		input : 인증키(key)  
		output : false(실패), true(성공)*/
	boolean isValidCode(String key) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
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
	
	/** HA-8 회원 삽입 (p.3 / 로그인+간편가입 팝업)
		input : 이메일(email), 비밀번호(pw)  
		output : - */
	void addMember(String email, String pw) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = "INSERT INTO members(member_id, email, nick_name, pw)"
				+ " VALUES(seq_member.nextval, ?, '익명 사용자', ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, pw);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	/** HA-9 프로필이미지 수정 (p.6 / 정보수정2)
		input : 회원아이디(memberId), 새이미지파일(profileImg)  
		output : - */
	void modifyProfileImg(String memberId, String profileImg) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "UPDATE members SET profile_img = ? WHERE member_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, profileImg);
		pstmt.setString(2, memberId);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-10 닉네임 수정 (p.6 / 정보수정2)
		input : 회원아이디(memberId), 새닉네임(nickName)
		output : - */
	void modifyNickName(int memberId, String nickName) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "UPDATE members SET nick_name = ? WHERE member_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, nickName);
		pstmt.setInt(2, memberId);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-11 이메일 수정 (p.6 / 정보수정2)
		input : 회원아이디(memberId), 새닉네임(email)  
		output : - */
	void modifyEmail(int memberId, String email) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE members SET email = ? WHERE member_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setInt(2, memberId);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-12 비밀번호 수정 (p.6 / 정보수정2)
		input : 회원아이디(memberId), 새비밀번호(pw)  
		output : - */
	void modifyPw(int memberId, String pw) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE members SET pw = ? WHERE member_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, pw);
		pstmt.setInt(2, memberId);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
//	/** HA-34 회원탈퇴 (p.6 / 정보수정2)
//		1. 공유한 사용자명에 있다면 공유사용자, 인증코드를 null로
//		2. 찜, 게시물, 블록, 댓글이 있다면 삭제
//		3. 회원 삭제
//		input : 탈퇴회원아이디  
//		output : - */
//	void delMember(int memberId) throws Exception {
//		String driver = "oracle.jdbc.driver.OracleDriver";
//		String url = "jdbc:oracle:thin:@localhost:1521:xe";
//		String dbId = "ab";
//		String dbPw = "12345";
//
//		Class.forName(driver);
//		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
//
//		String sql = "UPDATE boards SET share_user_id = '', key = '' WHERE share_user_id = ?"
//				
//				+ " DELETE FROM like_boards WHERE member_id = ?"
//				+ " DELETE FROM like_places WHERE  member_id = ?"
//				+ " DELETE FROM reviews WHERE user_id = ?"
//				+ " DELETE FROM comments WHERE writer_id = ?"
//				+ " DELETE FROM blocks"
//				+ " WHERE block_idx =" 
//				+ " (SELECT block_idx FROM boards bo INNER JOIN blocks bl ON bo.bno = bl.bno"
//				+ " WHERE writer_id = ?)"
//				+ " DELETE FROM boards WHERE writer_id = ?"
//
//				+ " DELETE FROM members WHERE member_id = ?";
//		
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		for(int i=1;i<9;i++) {			
//			pstmt.setInt(i, memberId);
//		}
//
//		pstmt.executeUpdate();
//
//		pstmt.close();
//		conn.close();
//	}
	
	public static void main(String[] args) throws Exception {
		MemberDao a = new MemberDao();
		System.out.println();
//		a.delMember(100);
		System.out.println("정상종료");
	}

}
