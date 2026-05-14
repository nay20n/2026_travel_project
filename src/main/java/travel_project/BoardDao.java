package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BoardDao {
	
	/** NY-5. 게시글 삽입 (p.7~10 / 일정표 만들기)
	    input :  작성자 id, 출발 장소 (start_place_id), 떠나고 싶은 도시 (arr_place_id), 출발날짜(start_date), 마지막날짜(end_date)
	    output : - */
	void insertBoard(int writerId, String placeId, String arrPlaceId, String startDate, String endDate) throws Exception{
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		//1. 연결 : “Connection 객체”
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		//2. SQL문 : “PreparedStatement 객체”
		String sql = "INSERT INTO boards(bno, writer_id, title, final_date, start_place_id, arr_place_id, start_date, end_date, transport_cost, food_cost, room_cost, etc_cost)\n"
				+ " VALUES (seq_board.nextval, ?, '내 게시글', SYSDATE, ?,?,?,?,0,0,0,0)\n";
		Statement stmt = conn.createStatement();
		
		//3. 결과테이블 : “ResultSet객체”
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();
		
	}
	
	/** NY-8. 게시글 찜 유무 조회(p.2 / 메인페이지, p.4 / 마이페이지, p.19 / 내 일정 게시글 2)
	    input: 로그인아이디(member_id), 게시글 번호(bno) 
        output: true(찜한거) flase(찜안한거) */
	boolean checkedLikeBoard(int memberId, int bno) throws Exception{
			//DB 접속 
			String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String dbId = "ab";
			String dbPw = "12345";
			
			//1. 연결 : “Connection 객체”
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url,dbId,dbPw);
			
			//2. SQL문 : “PreparedStatement 객체”
			String sql = "SELECT COUNT(*) \"찜 유무\"\n"
					+ "FROM like_boards lb \n"
					+ "INNER JOIN members m ON lb.member_id = m.member_id\n"
					+ "INNER JOIN boards b ON lb.bno = b.bno\n"
					+ "WHERE lb.member_id = ? AND b.bno = ?\n";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,memberId);
			pstmt.setInt(2,bno);
			
			//3. 결과테이블 : “ResultSet객체”
			ResultSet rs = pstmt.executeQuery();
			boolean isLiked = false;
		    if (rs.next()) { // 결과 행으로 이동 (필수!)
		        // 첫 번째 컬럼의 값을 가져와서 1인지 확인
		        isLiked = (rs.getInt(1) == 1);
		    }
		    return isLiked;
	}
	/** NY 9. 게시글 찜하기(p.2 / 메인페이지, p.4 / 마이페이지, p.19 / 내 일정 게시글 2)
	    input : 로그인 id, 게시글 번호(bno) 
	    output : - */
	void extraikeBoard(int memberId, int bno) throws Exception{
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		//1. 연결 : “Connection 객체”
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		//2. SQL문 : “PreparedStatement 객체”
		String sql = "INSERT INTO like_boards(member_id, bno, like_time) VALUES (?, ?, SYSDATE)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,memberId);
		pstmt.setInt(2,bno);
		
		//3. 결과테이블 : “ResultSet객체”
		pstmt.executeUpdate();
		
		System.out.println(memberId + "가 " + bno + "게시글을 좋아요 했습니다.");
	}
	/** NY-10. 게시글 찜 삭제하기 (p.2 / 메인페이지, p.4 / 마이페이지, p.19 / 내 일정 게시글 2)
	    input : 로그인 id, 삭제할 게시글 (bno) 
        output: - */
	void deleteLikeBoard(int memberId, int bno) throws Exception{
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		//1. 연결 : “Connection 객체”
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		//2. SQL문 : “PreparedStatement 객체”
		String sql = "DELETE FROM like_boards WHERE member_id = ? AND bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,memberId);
		pstmt.setInt(2,bno);
		
		//3. 결과테이블 : “ResultSet객체”
		pstmt.executeUpdate();
		
		System.out.println(memberId + "가 " + bno + "게시글의 좋아요를 취소했습니다.");
	}
	/** NY-11. 게시글 찜 개수 조회  (p.2 / 메인페이지, p.4 / 마이페이지, p.19 / 내 일정 게시글 2)
        input: 게시글 번호(bno)
		output :해당 게시글의 찜 개수 */
	int countLikeBoard(int bno) throws Exception{
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		//1. 연결 : “Connection 객체”
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		//2. SQL문 : “PreparedStatement 객체”
		String sql = "SELECT COUNT(*) \"게시글 찜 수\" FROM like_boards lb WHERE lb.bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bno);
		
		//3. 결과테이블 : “ResultSet객체”
		ResultSet rs = pstmt.executeQuery();
		int countLiked = 0;;
	    if (rs.next()) { // 결과 행으로 이동 (필수!)
	        
	    	countLiked = rs.getInt(1);
	    }
	    return countLiked;
	}
	
	public static void main(String[] args) throws Exception{
		
		BoardDao boardDto = new BoardDao();
		//boardDto.insertBoard(1, , null, null, null);
		
		//System.out.println(boardDto.checkedLikeBoard(1, 1)); // 게시글 찜 유무 
		//boardDto.extraikeBoard(1, 4); //1이 4게시글 좋아요 누르기 
		//boardDto.deleteLikeBoard(1, 4); //1이 4게시글 좋아요 취소하기 
		//System.out.println(boardDto.countLikeBoard(3)); // 3 게시글 좋아요 조회 

	}

}
