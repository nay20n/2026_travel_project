package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {
	
	/** NY-5. 게시글 삽입 (p.7~10 / 일정표 만들기)
	    input :  작성자 id, 출발 장소 (start_place_id), 떠나고 싶은 도시 (arr_place_id), 출발날짜(start_date), 마지막날짜(end_date)
	    output : - */
	void insertBoard(int writerId, String placeId, String arrPlaceId, String startDate, String endDate) throws Exception {
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
	boolean isLikeBoard(int memberId, int bno) throws Exception {
			//DB 접속 
			String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String dbId = "ab";
			String dbPw = "12345";
			
			//1. 연결 : “Connection 객체”
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url,dbId,dbPw);
			
			//2. SQL문 : “PreparedStatement 객체”
			String sql = "SELECT COUNT(*) \"찜 유무\" "
					+ "FROM like_boards lb "
					+ "INNER JOIN members m ON lb.member_id = m.member_id"
					+ "INNER JOIN boards b ON lb.bno = b.bno "
					+ "WHERE lb.member_id = ? AND b.bno = ? ";
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
	void extraLikeBoard(int memberId, int bno) throws Exception {
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
	void deleteLikeBoard(int memberId, int bno) throws Exception {
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
	int countLikeBoard(int bno) throws Exception {
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
	/** NY-12. 내 일정에 들어가 있는 장소 조회 (p.11 / 일정표 - 메인(주), p.15,16 / 일정표 - 메인(일)) - 결과가 공집합일 수도 있다. 이미지가 여러개일 경우 여러행이 조회됨
	 	input :  게시글번호(bno), 현재 보고 있는 member_id, 페이지 번호 
		output:  List<placeDto> */
	List<PlaceDto> getSelectedPlaces(int bno, int memberId, int page) throws Exception {
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		//1. 연결 : “Connection 객체”
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		//2. SQL문 : “PreparedStatement 객체”
		String sql = "SELECT "
				+ "    p.place_id, "
				+ "    p.name \"장소이름\", "
				+ "    p.category \"카테고리\", "
				+ "    p.address \"주소\", "
				+ "	   p.lat \"위도\", "
				+ "    p.lng \"경도\", "
				+ "    pi.image, "
				+ "    NVL(ROUND(AVG(r.rating), 1),0) \"평균별점\", "
				+ "    COUNT(DISTINCT r.review_idx) \"리뷰수\","
				+ "    (SELECT COUNT(*) FROM like_places lp WHERE member_id = ? AND lp.place_id = p.place_id) \"장소 찜 유무\" "
				+ "FROM blocks bl "
				+ "    INNER JOIN boards b ON b.bno = bl.bno "
				+ "    INNER JOIN places p ON bl.place_id = p.place_id "
				+ "    LEFT OUTER JOIN places_images pi ON bl.place_id = pi.place_id "
				+ "    LEFT OUTER JOIN reviews r ON bl.place_id = r.place_id "
				+ "WHERE b.bno = ? "
				+ "GROUP BY p.place_id, p.name, p.category, p.address, p.lat, p.lng, pi.image, pi.image_num "
				+ "ORDER BY MIN(bl.start_time), pi.image_num ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,memberId);
		pstmt.setInt(2,bno);
		
		//3. 결과테이블 : “ResultSet객체”
		ResultSet rs = pstmt.executeQuery(); 
		List <PlaceDto> placeDtos = new ArrayList<PlaceDto>();
	    while (rs.next()) { // 결과 행으로 이동 (필수!)
	    	String placeId = rs.getString("place_id");
	    	String name = rs.getString("장소이름");
	    	String category = rs.getString("카테고리");
	    	String address = rs.getString("주소");
	    	double lat = rs.getDouble("위도");
	    	double lng = rs.getDouble("경도");
	    	String img = rs.getString("image");
	    	double avgRating = rs.getDouble("평균별점");
	    	int reviewCnt = rs.getInt("리뷰수");
	    	boolean isLikedPlace = (rs.getInt("장소 찜 유무") == 1)? true : false;
	    	placeDtos.add(new PlaceDto(placeId, name, category, address, lat, lng, img, avgRating, reviewCnt, isLikedPlace));
	    }
	    
	    return placeDtos;
	}
	
	public static void main(String[] args) throws Exception {
		
		BoardDao boardDto = new BoardDao();
		//boardDto.insertBoard(1, , null, null, null);
		
		//System.out.println(boardDto.checkedLikeBoard(1, 1)); // 게시글 찜 유무 
		//boardDto.extraikeBoard(1, 4); //1이 4게시글 좋아요 누르기 
		//boardDto.deleteLikeBoard(1, 4); //1이 4게시글 좋아요 취소하기 
		//System.out.println(boardDto.countLikeBoard(3)); // 3 게시글 좋아요 조회 
		
//		List <PlaceDto> placeDtos = boardDto.getSelectedPlaces(1, 1, 0);// NY-12. (1이 1의 게시글을 보고 있을 경우)
//		for(int i=0; i<placeDtos.size(); i++) {
//			System.out.println("장소id : " + placeDtos.get(i).placeId);
//			System.out.println("장소이름 : " + placeDtos.get(i).name);
//			System.out.println("카테고리 : " + placeDtos.get(i).category);
//			System.out.println("주소 : " + placeDtos.get(i).address);
//			System.out.println("이미지 : " + placeDtos.get(i).img);
//			System.out.println("평균별점 : " + placeDtos.get(i).avgRating);
//			System.out.println("리뷰수 : " + placeDtos.get(i).reviewCnt);
//			System.out.println("찜 유무 : " + placeDtos.get(i).isLikedPlace);
//			System.out.println();
//		}
		
	}

}
