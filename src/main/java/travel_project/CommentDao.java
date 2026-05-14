package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CommentDao {
	/** NY-19. 댓글 개수 카운트 (p.19 / 내 일정 게시글)
	input : 현재 보고 있는 게시글 번호(bno) 
	output : 현재 보고 있는 게시글의 댓글 개수 */
	int CountComment(int bno) throws Exception {
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		//1. 연결 : “Connection 객체”
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		//2. SQL문 : “PreparedStatement 객체”
		String sql = "SELECT COUNT(*) \"게시글의 댓글 수\" "
				+ "FROM boards b "
				+ "INNER JOIN comments c ON b.bno = c.bno "
				+ "WHERE b.bno = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bno);
		
		//3. 결과테이블 : “ResultSet객체”
		ResultSet rs = pstmt.executeQuery();
		int CntComment = -1;
	    if (rs.next()) { // 결과 행으로 이동 (필수!)
	        // 첫 번째 컬럼의 값을 가져와서 1인지 확인
	    	CntComment = rs.getInt(1);
	    }
	    rs.close();
		pstmt.close();
		conn.close();
		
	    return CntComment;
	}

	public static void main(String[] args) throws Exception{
		 CommentDao commentDto = new CommentDao();
		 
		 //NY-18.
		 int bno = 1;
		 System.out.println("댓글 갯수 : " + commentDto.CountComment(bno));

	}

}
