package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CommentDao {
	/** NY-19. 댓글 개수 카운트 (p.19 / 내 일정 게시글) 
	 	input : 현재 보고 있는 게시글 번호(bno)
		output : 현재 보고 있는 게시글의 댓글 개수 */
	int CountComment(int bno) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT COUNT(*) \"게시글의 댓글 수\" "  
				+ "FROM boards b " + "INNER JOIN comments c ON b.bno = c.bno "
				+ "WHERE b.bno = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);

		ResultSet rs = pstmt.executeQuery();
		int CntComment = -1;
		if (rs.next()) {
			CntComment = rs.getInt("게시글의 댓글 수");
		}
		rs.close();
		pstmt.close();
		conn.close();

		return CntComment;
	}

	/** NY-20. 댓글 목록 조회 (p.19 / 내 일정 게시글) 
		input : 현재 보고 있는 게시글의 번호(bno), 페이지 번호
		output : List<CommentDto> */
	List<Map<String, Object>> getComment(int bno, int page) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT profile_img \"프로필 사진\", member_id \"멤버ID\" ,nick_name \"닉네임\", content \"내용\", final_date \"작성일\" "
				+ "FROM comments c INNER JOIN members m " + "ON c.writer_id = m.member_id " + "WHERE bno = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);

		ResultSet rs = pstmt.executeQuery();
//		List<CommentDto> commentDtos = new ArrayList<>();
		List<Map<String,Object>> mapList = new ArrayList<>();
		while (rs.next()) {
			Map<String, Object> tempMap= new HashMap<String,Object>();
			tempMap.put("profileImg",rs.getString("프로필 사진"));
			tempMap.put("memberId",rs.getInt("멤버ID"));
			tempMap.put("writerNick",rs.getString("닉네임"));
			tempMap.put("content",rs.getString("내용"));
			tempMap.put("date",rs.getString("작성일"));
			mapList.add(tempMap);
		}
		rs.close();
		pstmt.close();
		conn.close();

		return mapList;
	}

	/** HA-32 게시글 댓글 삭제 (p.19 / 내 일정 게시글2) 해당사용자가 게시글 작성자 또는 댓글 작성자인지 확인 필요
		input : 댓글번호, 지울려는 member_id
		output : - */
	void deleteComment(int cno, int id) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "DELETE "
				+ "FROM (SELECT *  "
				+ "    FROM comments c INNER JOIN boards b ON c.bno = b.bno "
				+ "    WHERE (c.writer_id = ? OR b.writer_id = ?))  "
				+ "WHERE cno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.setInt(2, id);
		pstmt.setInt(3, cno);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-33 게시글 댓글 추가 (p.19 / 내 일정 게시글2)
		input : 게시글번호(bno), 작성자아이디, 내용  
		output : - */
	void insertComment(int bno, int writerId, String content) throws Exception {
		// DB 접속
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
	
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
	
		String sql = "INSERT INTO comments(cno, bno, writer_id, content, final_date) \n"
				+ "VALUES(seq_comment.nextval, ?, ?, ?, SYSDATE) ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);
		pstmt.setInt(2, writerId);
		pstmt.setString(3, content);
	
		pstmt.executeUpdate();
	
		pstmt.close();
		conn.close();
	}
	
	/** HA-34 게시글 댓글 수정 (p.19 / 내 일정 게시글2)
	input : 댓글번호(cno), 수정 할 내용(content)  
	output : - */
	void modifyComment(int cno, String content) throws Exception {
		// DB 접속
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
	
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
	
		String sql = "UPDATE comments SET content=? WHERE cno=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, content);
		pstmt.setInt(2, cno);
		
	
		pstmt.executeUpdate();
	
		pstmt.close();
		conn.close();
	}
	
	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
		CommentDao commentDto = new CommentDao();
		 
		 //NY-19.
//		 int bno = 1;
//		 System.out.println("댓글 갯수 : " + commentDto.CountComment(bno));
		 
		 //NY-20.
//		 int bno = 2;
//		 int page = 1;
//		 List<Map<String,Object>> list = commentDto.getComment(bno, page);
//		 for(int i=0; i<list.size(); i++) {
//			 System.out.println(list.get(i));
////			 System.out.println(list.get(i).get("memberId").getClass());
//		 }
		 
		 //HA-32.
//		 System.out.print("삭제할 댓글 번호 : ");
//		 int cno = sc.nextInt();
//		 System.out.print("지울려는 사람의 id : ");
//		 int id = sc.nextInt();
//		 commentDto.deleteComment(cno, id);
		 
		//HA-33.
//		System.out.print("추가할 게시글 번호 : ");
//		int bno = sc.nextInt();
//		System.out.print("작성할 아이디 : ");
//		int id = sc.nextInt();
//		System.out.print("댓글 : ");
//		String content  = sc.next();
//		commentDto.insertComment(bno, id, content);
		 
		 //HA-34.
		System.out.print("수정할 댓글 번호:");
		int cno = sc.nextInt();
		System.out.print("바꿀 내용:");
		String content = sc.next();
		commentDto.modifyComment(cno, content);
	}

}
