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

public class BoardDao {
	
	/** NY-5. 게시글 삽입 (p.7~10 / 일정표 만들기)
	    input :  작성자 id, 출발 장소 (start_place_id), 떠나고 싶은 도시 (arr_place_id), 출발날짜(start_date), 마지막날짜(end_date),여행지의 행정구역(arr_place_city)
	    output : - */
	int insertBoard(int writerId, String startPlaceId, String arrPlaceId, String startDate, String endDate, String arrPlaceCity) throws Exception {
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		//1. 연결 : “Connection 객체”
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		//2. SQL문 : “PreparedStatement 객체”
		String sql = "INSERT INTO boards(bno, writer_id, title, final_date, start_place_id, arr_place_id, start_date, end_date, transport_cost, food_cost, room_cost, etc_cost, arr_place_city)"
				+ "VALUES (seq_board.nextval, ?, '내 게시글', SYSDATE, ?, ?, TO_DATE(?, 'YYYY-MM-DD'),TO_DATE(?, 'YYYY-MM-DD'),0,0,0,0, ?)";
		String genealColumns []= {"BNO"};
		PreparedStatement pstmt = conn.prepareStatement(sql,genealColumns);
		
		pstmt.setInt(1, writerId);
		pstmt.setString(2, startPlaceId);
		pstmt.setString(3, arrPlaceId);
		pstmt.setString(4, startDate);
		pstmt.setString(5, endDate);
		pstmt.setString(6, arrPlaceCity);
		
		//3. 결과테이블 : “ResultSet객체”
		pstmt.executeUpdate();
		
		ResultSet rs = pstmt.getGeneratedKeys(); 
		int newBno = -1;
		
	    if (rs.next()) {
	    	newBno = rs.getInt(1);
	    }
		
	    rs.close();
		pstmt.close();
		conn.close();
		
		return newBno;
		
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
	    rs.close();
		pstmt.close();
		conn.close();
		
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

		pstmt.close();
		conn.close();
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
		
		pstmt.close();
		conn.close();
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
	    
	    rs.close();
		pstmt.close();
		conn.close();
		
	    return countLiked;
	}
	/** NY-12. 내 일정에 들어가 있는 장소 조회 (p.11 / 일정표 - 메인(주), p.15,16 / 일정표 - 메인(일)) - 결과가 공집합일 수도 있다. 이미지가 여러개일 경우 여러행이 조회됨
	 	input :  게시글번호(bno), 현재 보고 있는 member_id, 페이지 번호 
		output:  List<Map<String,Object>> */
	List<Map<String,Object>> getSelectedPlaces(int bno, int memberId, int page) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
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
		
		ResultSet rs = pstmt.executeQuery(); 
		List<Map<String,Object>> list = new ArrayList<>();
	    while (rs.next()) {
	    	Map<String,Object> tempMap = new HashMap<>();
	    	tempMap.put("placeId", rs.getString("place_id"));
	    	tempMap.put("name", rs.getString("장소이름"));
	    	tempMap.put("category", rs.getString("카테고리"));
	    	tempMap.put("address", rs.getString("주소"));
	    	tempMap.put("lat", rs.getObject("위도", Double.class));
	    	tempMap.put("lng", rs.getObject("경도", Double.class));
	    	tempMap.put("img", rs.getString("image"));
	    	tempMap.put("avgRating", rs.getDouble("평균별점"));
	    	tempMap.put("reviewCnt", rs.getInt("리뷰수"));
	    	tempMap.put("isLikedPlace", rs.getInt("장소 찜 유무") == 1);
	    	list.add(tempMap);
	    }
	    
	    rs.close();
		pstmt.close();
		conn.close();
		
	    return list;
	}
	/** NY-13. 찜한 장소들 조회 (p.11 / 일정표 - 메인(주), p.15,16 / 일정표 - 메인(일)) - 결과가 공집합일 수도 있다, 이미지가 여러개일 경우 여러행이 조회됨  (최근에 찜한 순)
		input : 수정중인게시글번호(bno), 조회하고 있는 회원 아이디(member_id), 게시글의 여행지(place_id), 페이지 번호 
		output: List<Map<String,Object>>(장소이름, 카테고리, 주소, 이미지들) */
	List<Map<String,Object>> getLikedPlaces(int bno, int memberId, String arrPlaceId, int page) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "SELECT "
				+ "    p.place_id , "
				+ "    p.name \"장소이름\", "
				+ "    p.category \"카테고리\", "
				+ "    p.address \"주소\", "
				+ "    p.lat \"위도\", "
				+ "    p.lng \"경도\", "
				+ "    pi.image, "
				+ "    NVL(ROUND(AVG(r.rating), 1),0) \"평균별점\", "
				+ "    COUNT(DISTINCT r.review_idx) \"리뷰수\", "
				+ "    (SELECT COUNT(*) FROM like_places lp WHERE member_id = ? AND lp.place_id = p.place_id) \"장소 찜 유무\" "
				+ "FROM like_places lp "
				+ "    INNER JOIN  places p ON lp.place_id = p.place_id "
				+ "    LEFT OUTER JOIN reviews r ON r.place_id = lp.place_id "
				+ "    LEFT OUTER JOIN places_images pi ON p.place_id = pi.place_id "
				+ "WHERE lp.member_id = ? "
				+ "        AND (6371 * ACOS( "
				+ "               COS( (SELECT lat FROM places WHERE place_id = ?) * (ACOS(-1)/180) )  "
				+ "               * COS( p.lat * (ACOS(-1)/180) ) "
				+ "               * COS( (p.lng * (ACOS(-1)/180)) - (SELECT lng FROM places WHERE place_id = ?) * (ACOS(-1)/180)) "
				+ "               + SIN( (SELECT lat FROM places WHERE place_id = ?) * (ACOS(-1)/180) ) "
				+ "               * SIN( p.lat * (ACOS(-1)/180) ) "
				+ "           )) <= 20 OR p.address LIKE (SELECT arr_place_city FROM boards WHERE bno = ?) "
				+ "GROUP BY p.place_id, p.name, p.category, p.address, pi.image, pi.image_num, p.lat, p.lng "
				+ "ORDER BY MAX(lp.like_time), pi.image_num ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,memberId);
		pstmt.setInt(2,memberId);
		pstmt.setString(3,arrPlaceId);
		pstmt.setString(4,arrPlaceId);
		pstmt.setString(5,arrPlaceId);
		pstmt.setInt(6, bno);
		
		ResultSet rs = pstmt.executeQuery(); 
		List<Map<String,Object>> list = new ArrayList<>();
	    while (rs.next()) {
	    	Map<String,Object> tempMap = new HashMap<>();
	    	tempMap.put("placeId", rs.getString("place_id"));
	    	tempMap.put("name", rs.getString("장소이름"));
	    	tempMap.put("category", rs.getString("카테고리"));
	    	tempMap.put("address", rs.getString("주소"));
	    	tempMap.put("lat", rs.getDouble("위도"));
	    	tempMap.put("lng", rs.getDouble("경도"));
	    	tempMap.put("img", rs.getString("image"));
	    	tempMap.put("avgRating", rs.getDouble("평균별점"));
	    	tempMap.put("reviewCnt", rs.getInt("리뷰수"));
	    	tempMap.put("isLikedPlace", rs.getInt("장소 찜 유무") == 1);
	    	list.add(tempMap);
	    }
	    
	    rs.close();
		pstmt.close();
		conn.close();
		
	    return list;
	}
	/** NY-14.게시글  제목 수정 (p.11 / 일정표 - 메인(주), p.15,16 / 일정표 - 메인(일), p.17 / 일정표 - 메인(월), p.18/ 내 일정 게시글1)
		input : 바꿀 제목(title), 현재 수정 중인 게시글 번호(bno) 
		output : - */
	void modifyTitle(String title, int bno) throws Exception {
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		//1. 연결 : “Connection 객체”
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		//2. SQL문 : “PreparedStatement 객체”
		String sql = "UPDATE boards SET title = ?, final_date = SYSDATE WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,title);
		pstmt.setInt(2, bno);
		
		//3. 결과테이블 : “ResultSet객체”
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	/** NY-15. 장소 검색 조회 (p.11 / 일정표 - 메인(주), p.15,16 / 일정표 - 메인(일)) - 찜여부 
		input : 검색중인 memberId, 검색키워드(장소이름, 카테고리, 주소로 조회 가능), 여행지(place_id), 수정중인게시글번호(bno) 페이지 번호 
		output : List<Map<String,Object>>*/
	List<Map<String,Object>> getSerchedPlace(int memberId, String content, String arrPlaceId, int bno, int page) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "SELECT "
				+ "    p.place_id ,"
				+ "    p.name \"장소이름\", "
				+ "    p.category \"카테고리\", "
				+ "    p.address \"주소\", "
				+ "    p.lat \"위도\", "
				+ "    p.lng \"경도\", "
				+ "    NVL(ROUND(AVG(r.rating), 1),0) \"평균별점\", "
				+ "    COUNT(DISTINCT r.review_idx) \"리뷰수\", "
				+ "    (SELECT COUNT(*) FROM like_places lp WHERE member_id = ? AND lp.place_id = p.place_id) \"장소 찜 유무\", "
				+ "    pi.image\n"
				+ "FROM places p \n"
				+ "LEFT OUTER JOIN places_images pi ON p.place_id = pi.place_id "
				+ "LEFT OUTER JOIN reviews r ON p.place_id = r.place_id "
				+ "WHERE ( "
				+ "name LIKE ? "
				+ "	OR category LIKE ? "
				+ "	OR address LIKE ? )"
				+ "	AND (6371 * ACOS( "
				+ "               COS( (SELECT lat FROM places WHERE place_id = ?) * (ACOS(-1)/180) ) "
				+ "               * COS( p.lat * (ACOS(-1)/180) ) \n"
				+ "               * COS( (p.lng * (ACOS(-1)/180)) - (SELECT lng FROM places WHERE place_id = ?) * (ACOS(-1)/180)) "
				+ "               + SIN( (SELECT lat FROM places WHERE place_id = ?) * (ACOS(-1)/180) )  "
				+ "               * SIN( p.lat * (ACOS(-1)/180) ) "
				+ "           )) <= 20 OR p.address LIKE (SELECT arr_place_city FROM boards WHERE bno = ?) "
				+ "GROUP BY  p.place_id, p.name, p.category, p.address, pi.image, pi.image_num, p.lat, p.lng "
				+ "ORDER BY 리뷰수 DESC, 장소이름 DESC, image_num ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,memberId);
		pstmt.setString(2,"%"+content+"%");
		pstmt.setString(3,"%"+content+"%");
		pstmt.setString(4,"%"+content+"%");
		pstmt.setString(5,arrPlaceId);
		pstmt.setString(6,arrPlaceId);
		pstmt.setString(7,arrPlaceId);
		pstmt.setInt(8, bno);
		
		ResultSet rs = pstmt.executeQuery(); 
		List<Map<String,Object>> list = new ArrayList<>();
	    while (rs.next()) {
	    	Map<String,Object> tempMap = new HashMap<>();
	    	tempMap.put("placeId", rs.getString("place_id"));
	    	tempMap.put("name", rs.getString("장소이름"));
	    	tempMap.put("category", rs.getString("카테고리"));
	    	tempMap.put("address", rs.getString("주소"));
	    	tempMap.put("lat", rs.getDouble("위도"));
	    	tempMap.put("lng", rs.getDouble("경도"));
	    	tempMap.put("img", rs.getString("image"));
	    	tempMap.put("avgRating", rs.getDouble("평균별점"));
	    	tempMap.put("reviewCnt", rs.getInt("리뷰수"));
	    	tempMap.put("isLikedPlace", rs.getInt("장소 찜 유무") == 1);
	    	list.add(tempMap);
	    }
	    
	    rs.close();
		pstmt.close();
		conn.close();
	    
	    return list;
	}
	
	/** NY-16. 공유 버튼 클릭 시 인증코드 및 만료일시 생성  (p.11 / 일정표 - 메인(주), p.15,16 / 일정표 - 메인(일), p.17 / 일정표 - 메인(월))
		input : 현재 공유하려고 하는 게시글 번호(bno) 
		output : -*/
	void createShareKey(int bno) throws Exception{
		//랜덤 키 
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "UPDATE boards SET key = ?, ex_date = (SYSDATE + 1 / 24/10) WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,sb.toString());
		pstmt.setInt(2, bno);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	/** NY-17. 공유 인증코드를 입력해서 공유회원이 들어온 경우 (p.11 / 일정표 - 메인(주), p.15,16 / 일정표 - 메인(일), p.17 / 일정표 - 메인(월))
		input : 공동작업자 id(member_id), 공유하려고 하는 게시글 번호(bno), 인증코드(key), 만료일시(ex_date)  
		output : -*/
	void addSharedMember(int memberId, int bno, String key) throws Exception{
		
		//DB 접속 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "UPDATE boards SET share_user_id = ? WHERE bno = ? AND share_user_id IS NULL AND key = ? AND ex_date > SYSDATE";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setInt(2, bno);
		pstmt.setString(3, key);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	/** NY-18. 일정 날짜 추가 및 삭제  (p.17 / 일정표 - 메인(월 단위))
		input : 바꿀 게시글 (bno), 바뀐 시작날짜, 바뀐 끝난 날짜  
		output : - 
		(주의) 날짜를 지웠을 경우, 해당 하루치 블럭들 삭제 필수 */
	void modifyTravelDate(int bno, String startDate, String endDate) throws Exception{
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "UPDATE boards SET start_date = TO_DATE(?, 'YYYY-MM-DD'), end_date =  TO_DATE(?, 'YYYY-MM-DD') , final_date = SYSDATE WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, startDate);
		pstmt.setString(2, endDate);
		pstmt.setInt(3, bno);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	/** NY-21. 게시글 업로드 할 때마다 최종 수정일 업데이트 (p.10, p.11,p.15, p.16, p.17, p.18)
		input : 업로드 하는 게시글의 번호 (bno)  
		output : -*/
	void updateFinalDate(int bno) throws Exception{
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "UPDATE boards SET final_date = SYSDATE WHERE bno=?" ;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	/** *NY-22.  게시글 정보 조회 (p.18 / 내 일정 게시글1)
		input : 로그인 한 사람의 id(member.id), 게시글 번호(bno)
		output : 해당 게시글의 Map<String,Object> */
	Map<String,Object> getBoardInfo(int memberId, int bno) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "SELECT "
				+ "    b.writer_id \"작성자Id\","
				+ "    b.title \"게시글 제목\", "
				+ "    b.start_place_id \"출발지\", "
				+ "    b.arr_place_id \"도착지\", "
				+ "    b.start_date \"출발날짜\", b.end_date \"도착날짜\", "
				+ "    b.max_cost \"최대경비\", b.transport_cost \"교통비\", b.food_cost \"식비\", b.room_cost \"숙소비\", b.etc_cost \"기타경비\", "
				+ "    (SELECT COUNT(*) FROM comments c WHERE b.bno = c.bno) \"게시글의 댓글 수\", "
				+ "    (SELECT COUNT(*) FROM like_boards lb WHERE lb.member_id = ? AND lb.bno = b.bno)\"찜 유무\", "
				+ "    (SELECT COUNT(*)FROM like_boards lb WHERE lb.bno = b.bno) \"찜 개수\", "
				+ "    bl.block_idx \"블럭 인덱스\", "
				+ "    bl.start_time \"블럭 시작시간\", "
				+ "    bl.end_time \"블럭 끝시간\", "
				+ "    bl.color_idx \"블럭 색깔 인덱스\", "
				+ "    c.color_code \"색깔 code\", "
				+ "    p.name \"장소 이름\" "
				+ "FROM boards b "
				+ "LEFT OUTER JOIN blocks bl ON b.bno=bl.bno "
				+ "LEFT OUTER JOIN places p ON bl.place_id=p.place_id "
				+ "INNER JOIN color c ON bl.color_idx = c.color_idx "
				+ "WHERE b.bno = ? " ;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,memberId);
		pstmt.setInt(2, bno);
		
		ResultSet rs = pstmt.executeQuery(); 
		Map<String,Object> tempMap = new HashMap<>();
	    if (rs.next()) {
	    	tempMap.put("writerId", rs.getInt("작성자Id"));
	    	tempMap.put("title", rs.getString("게시글 제목"));
	    	tempMap.put("startPlaceId", rs.getString("출발지"));
	    	tempMap.put("arrPlaceId", rs.getString("도착지"));
	    	tempMap.put("startDate", rs.getString("출발날짜"));
	    	tempMap.put("endDate", rs.getString("도착날짜"));
	    	tempMap.put("maxCost", rs.getObject("최대경비",Integer.class));
	    	tempMap.put("transportCost", rs.getInt("교통비"));
	    	tempMap.put("foodCost", rs.getInt("식비"));
	    	tempMap.put("roomCost", rs.getInt("숙소비"));
	    	tempMap.put("etcCost", rs.getInt("기타경비"));
	    	tempMap.put("commentCnt", rs.getInt("게시글의 댓글 수"));
	    	tempMap.put("isLikeBoard", rs.getInt("찜 유무") == 1);
	    	tempMap.put("likeBoardCnt", rs.getInt("찜 개수"));
	    	tempMap.put("blockIdx", rs.getInt("블럭 인덱스"));
	    	tempMap.put("startTime", rs.getString("블럭 시작시간"));
	    	tempMap.put("endTime", rs.getString("블럭 시작시간"));
	    	tempMap.put("colorIdx", rs.getInt("블럭 색깔 인덱스"));
	    	tempMap.put("colorCode", rs.getString("색깔 code"));
	    	tempMap.put("name", rs.getString("장소 이름"));
	    }
	    
	    rs.close();
		pstmt.close();
		conn.close();
	    
	    return tempMap;
	}
	
	/** NY-24. -a. 타인의 일정 게시글 복제 (p.20 / 타인의 게시글)
		input : 복제하려는 게시글 번호(bno), 복제하려는 회원 id(member_id), 출발지(start_place_id), 도착지(arr_place_id) ,여행기간(start_date), 복제된 게시글의 bno
		output : - 
		보드 복사 후 바로 블럭 복제 해야됨 
		*/
	void copyBoard(int bno, int memberId, String startPlaceId, String startDate) throws Exception{
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url,dbId,dbPw);
		
		String sql = "INSERT INTO boards( "
				+ "bno, writer_id, title, final_date, start_place_id, arr_place_id, start_date, end_date, transport_cost, food_cost, room_cost, etc_cost, arr_place_city)"
				+ "SELECT "
				+ "SEQ_BOARD.NEXTVAL, "
				+ "	?, "
				+ "	'내 게시글', "
				+ "	SYSDATE, "
				+ "?, "
				+ "arr_place_id, "
				+ "TO_DATE(?, 'YYYY-MM-DD'), "
				+ "TO_DATE(?, 'YYYY-MM-DD') + (end_date - start_date), "
				+ "transport_cost, "
				+ "food_cost, "
				+ "room_cost, "
				+ "etc_cost, "
				+ "arr_place_city "
				+ "FROM boards "
				+ "WHERE bno = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setString(2, startPlaceId);
		pstmt.setString(3, startDate);
		pstmt.setString(4, startDate);
		pstmt.setInt(5, bno);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	/** HA-2. 최신 게시글 조회 (p.2 / 메인페이지)
	 	a.로그인 버전
		input : memberId(로그인아이디), page(페이지번호)
		output : List<Map<String,Object>>
		주의: 장소가 들어간 블록이 있는 게시글만 - 지도 그려야해서. */
	List<Map<String,Object>> showBoardsLatestOrder(int memberId, int page) throws Exception {
		List<Map<String,Object>> list = new ArrayList<>();
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT bo.bno, bo.title,"
				+ " EXTRACT(YEAR FROM bo.start_date) 여행년도,"
				+ " EXTRACT(MONTH FROM bo.start_date) 여행월,"
				+ " SYSDATE - bo.final_date 수정경과시간,"
				+ " (SELECT COUNT(*) FROM like_boards lb WHERE lb.bno=bo.bno) 찜수,"
				+ " (SELECT COUNT(*) FROM like_boards lb WHERE lb.member_id = ? AND lb.bno = bo.bno) \"찜 유무\","
				+ " p.lat, p.lng"
				+ " FROM boards bo INNER JOIN blocks bl"
				+ " ON bo.bno = bl.bno"
				+ " INNER JOIN places p"
				+ " ON bl.place_id = p.place_id"
				+ " ORDER BY bo.final_date DESC, bl.start_time ASC";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);

		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("bno", rs.getInt("bno"));
			tempMap.put("title", rs.getString("title"));
			tempMap.put("year", rs.getInt("여행년도"));
			tempMap.put("month", rs.getInt("여행월"));
			
			// 수정 경과시간을 문자열 형태로 바꾸기
			double temp = rs.getDouble("수정경과시간");
			StringBuffer sb = new StringBuffer();
			if(temp>14) sb.append("오래");
			else if(temp>1) sb.append((int)temp + "일");
			else if(temp/24>1) sb.append((int)(temp/24) + "시간");
			else sb.append((int)(temp/24/60) + "분");
			sb.append("전");
			tempMap.put("elapsedTime", sb.toString());
			
			tempMap.put("likedBoardCnt", rs.getInt("찜수"));
			tempMap.put("isLikedBoard", rs.getInt("찜 유무")==1);
			tempMap.put("lat", rs.getDouble("lat"));
			tempMap.put("lng", rs.getDouble("lng"));
			
			list.add(tempMap);
		}
				
		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}
	
	/** HA-2. 최신 게시글 조회 (p.2 / 메인페이지)
 		b.로그아웃 버전 (찜수 표시x)
		input : page(페이지번호)
		output : List<Map<String,Object>>
		주의: 장소가 들어간 블록이 있는 게시글만 - 지도 그려야해서. */
	List<Map<String,Object>> showBoardsLatestOrder(int page) throws Exception {
		List<Map<String,Object>> list = new ArrayList<>();

		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT bo.bno, bo.title," 
				+ " EXTRACT(YEAR FROM bo.start_date) 여행년도,"
				+ " EXTRACT(MONTH FROM bo.start_date) 여행월," 
				+ " SYSDATE - bo.final_date 수정경과시간,"
				+ " (SELECT COUNT(*) FROM like_boards lb WHERE lb.bno=bo.bno) 찜수,"
				+ " p.lat, p.lng" 
				+ " FROM boards bo INNER JOIN blocks bl" 
				+ " ON bo.bno = bl.bno"
				+ " INNER JOIN places p" 
				+ " ON bl.place_id = p.place_id"
				+ " ORDER BY bo.final_date DESC, bl.start_time ASC";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("bno", rs.getInt("bno"));
			tempMap.put("title", rs.getString("title"));
			tempMap.put("year", rs.getInt("여행년도"));
			tempMap.put("month", rs.getInt("여행월"));

			// 수정 경과시간을 문자열 형태로 바꾸기
			double temp = rs.getDouble("수정경과시간");
			StringBuffer sb = new StringBuffer();
			if(temp>14) sb.append("오래");
			else if(temp>1) sb.append((int)temp + "일");
			else if(temp/24>1) sb.append((int)(temp/24) + "시간");
			else sb.append((int)(temp/24/60) + "분");
			sb.append("전");
			tempMap.put("elapsedTime", sb.toString());

			tempMap.put("likedBoardCnt", rs.getInt("찜수"));
			tempMap.put("lat", rs.getDouble("lat"));
			tempMap.put("lng", rs.getDouble("lng"));
			
			list.add(tempMap);
		}

		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}
	
	/** HA-3. 검색으로 게시글 조회 (p.2 / 메인페이지)
	  	a. 로그인버전
		input : input(검색키워드), memberId(로그인아이디), page(페이지번호)
		output : List<Map<String,Object>>
		주의: 장소가 들어간 블록이 있는 게시글만 - 지도 그려야해서. */
	List<Map<String,Object>> showBoardsKeyOrder(String input, int memberId, int page) throws Exception {
		List<Map<String,Object>> list = new ArrayList<>();

		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT bo.bno, bo.title,"
				+ " EXTRACT(YEAR FROM bo.start_date) 여행년도,"
				+ " EXTRACT(MONTH FROM bo.start_date) 여행월,"
				+ " SYSDATE - bo.final_date 수정경과시간,"
				+ " (SELECT COUNT(*) FROM like_boards lb WHERE lb.bno = bo.bno) 찜수,"
				+ " (SELECT COUNT(*) FROM like_boards lb WHERE lb.member_id = ? AND lb.bno = bo.bno) \"찜 유무\","
				+ " p.lat, p.lng"
				+ " FROM boards bo INNER JOIN blocks bl"
				+ " ON bo.bno = bl.bno"
				+ " INNER JOIN places p"
				+ " ON bl.place_id = p.place_id"
				+ " AND ((bo.title LIKE ? )"
				+ " OR (p.address LIKE ? ))"
				+ " ORDER BY 찜수 DESC";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setString(2, "%" + input + "%");
		pstmt.setString(3, "%" + input + "%");

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("bno", rs.getInt("bno"));
			tempMap.put("title", rs.getString("title"));
			tempMap.put("year", rs.getInt("여행년도"));
			tempMap.put("month", rs.getInt("여행월"));

			// 수정 경과시간을 문자열 형태로 바꾸기
			double temp = rs.getDouble("수정경과시간");
			StringBuffer sb = new StringBuffer();
			if(temp>14) sb.append("오래");
			else if(temp>1) sb.append((int)temp + "일");
			else if(temp/24>1) sb.append((int)(temp/24) + "시간");
			else sb.append((int)(temp/24/60) + "분");
			sb.append("전");
			tempMap.put("elapsedTime", sb.toString());

			tempMap.put("likedBoardCnt", rs.getInt("찜수"));
			tempMap.put("isLikedBoard", rs.getInt("찜 유무")==1);
			tempMap.put("lat", rs.getDouble("lat"));
			tempMap.put("lng", rs.getDouble("lng"));
			
			list.add(tempMap);
		}

		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}
	
	/** HA-3. 검색으로 게시글 조회 (p.2 / 메인페이지) 
	 	b. 로그아웃 버전 (찜수 제외)
	 	input : input(검색키워드),page(페이지번호) 
		output : List<Map<String,Object>>
		주의: 장소가 들어간 블록이 있는 게시글만 - 지도 그려야해서. */
	List<Map<String,Object>> showBoardsKeyOrder(String input, int page) throws Exception {
		List<Map<String,Object>> list = new ArrayList<>();

		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT bo.bno, bo.title,"
				+ " EXTRACT(YEAR FROM bo.start_date) 여행년도,"
				+ " EXTRACT(MONTH FROM bo.start_date) 여행월,"
				+ " SYSDATE - bo.final_date 수정경과시간,"
				+ "(SELECT COUNT(*) FROM like_boards lb WHERE lb.bno = bo.bno) 찜수,"
				+ " p.lat, p.lng"
				+ " FROM boards bo INNER JOIN blocks bl" 
				+ " ON bo.bno = bl.bno"
				+ " INNER JOIN places p" 
				+ " ON bl.place_id = p.place_id" 
				+ " AND ((bo.title LIKE ? )"
				+ " OR (p.address LIKE ? ))" 
				+ " ORDER BY 찜수 DESC";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, "%" + input + "%");
		pstmt.setString(2, "%" + input + "%");

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("bno", rs.getInt("bno"));
			tempMap.put("title", rs.getString("title"));
			tempMap.put("year", rs.getInt("여행년도"));
			tempMap.put("month", rs.getInt("여행월"));

			// 수정 경과시간을 문자열 형태로 바꾸기
			double temp = rs.getDouble("수정경과시간");
			StringBuffer sb = new StringBuffer();
			if(temp>14) sb.append("오래");
			else if(temp>1) sb.append((int)temp + "일");
			else if(temp/24>1) sb.append((int)(temp/24) + "시간");
			else sb.append((int)(temp/24/60) + "분");
			sb.append("전");
			tempMap.put("elapsedTime", sb.toString());

			tempMap.put("likedBoardCnt", rs.getInt("찜수"));
			tempMap.put("lat", rs.getDouble("lat"));
			tempMap.put("lng", rs.getDouble("lng"));
			
			list.add(tempMap);
		}

		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}
	
	/** HA-31 여행경비 수정 (p.18 / 내 일정 게시글1)
		a. AI 업데이트 시
		input : bno(게시글번호), maxCost(새AI예상최대비용), transportCost(새교통비), foodCost(새식비), roomCost(새숙소비), etcCost(새기타경비)  
		output : - */
	void modifyCostAi(int bno, int maxCost, int transportCost, int foodCost, int roomCost, int etcCost) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "UPDATE boards SET max_cost = ?, transport_cost = ?, food_cost = ?, room_cost = ?, etc_cost = ?, final_date = SYSDATE WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, maxCost);
		pstmt.setInt(2, transportCost);
		pstmt.setInt(3, foodCost);
		pstmt.setInt(4, roomCost);
		pstmt.setInt(5, etcCost);
		pstmt.setInt(6, bno);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-31 여행경비 수정 (p.18 / 내 일정 게시글1)
		b. 교통비
		input : bno(게시글번호), transportCost(새교통비)
		output : - */
	void modifyTransportCost(int bno, int transportCost) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE  boards SET transport_cost = ?, final_date = SYSDATE WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, transportCost);
		pstmt.setInt(2, bno);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-31 여행경비 수정 (p.18 / 내 일정 게시글1)
		c. 식비
		input : bno(게시글번호), foodCost(새교통비)
		output : - */
	void modifyFoodCost(int bno, int foodCost) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE  boards SET food_cost = ?, final_date = SYSDATE WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, foodCost);
		pstmt.setInt(2, bno);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-31 여행경비 수정 (p.18 / 내 일정 게시글1)
		d. 숙소비
		input : bno(게시글번호), roomCost(새교통비)
		output : - */
	void modifyRoomCost(int bno, int roomCost) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE  boards SET room_cost = ?, final_date = SYSDATE WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, roomCost);
		pstmt.setInt(2, bno);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-31 여행경비 수정 (p.18 / 내 일정 게시글1)
		e. 기타경비
		input : bno(게시글번호), etcCost(새교통비)
		output : - */
	void modifyetcCost(int bno, int etcCost) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE  boards SET etc_cost = ?, final_date = SYSDATE WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, etcCost);
		pstmt.setInt(2, bno);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-35 추천여행지 조회 (p.8 / 일정표 만들기-떠나고 싶은 도시는? -1)
		input : -
		output : List<Map<String,Object>> */
	List<Map<String,Object>> viewRecommendedPlace() throws Exception {
		List<Map<String,Object>> list = new ArrayList<>();
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT r.place_id, r.spot, p.lat, p.lng"
				+ " FROM recommended_places r INNER JOIN places p"
				+ "	ON r.place_id = p.place_id"
				+ " ORDER BY num";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("rcPlaceId", rs.getString("place_id"));
			tempMap.put("name", rs.getString("spot"));
			tempMap.put("lat", rs.getDouble("lat"));
			tempMap.put("lng", rs.getDouble("lng"));
			list.add(tempMap);
		}
				
		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}
	
	/** HA-36 게시글 삭제 (p.18 / 내 일정 게시글 1)
		input : bno(게시글번호)
		output : - 
		주의: 블럭이 있다면 삭제할 수 없음 */
	void delBoard(int bno) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "DELETE FROM boards WHERE bno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		BoardDao boardDao = new BoardDao();
		Scanner sc = new Scanner(System.in);
		
		//NY-5.
//		int writerId = 1;
//		String startPlaceId = "ChIJA3CU42aifDURaq-3csGXvuc";
//		String arrPlaceId = "ChIJFaft2p-NaDURpWUIUUDNxCE";
//		String startDate = "20240404";
//		String endDate = "20240406";
//		String arrPlaceCity = "부산";
//		int newBno = boardDao.insertBoard(writerId,startPlaceId,arrPlaceId,startDate,endDate,arrPlaceCity); // NY-5.
//		System.out.println(newBno);
		
		//System.out.println(boardDao.checkedLikeBoard(1, 1)); // NY-8. 게시글 찜 유무 
		//boardDao.extraikeBoard(1, 4); // NY-9. 1이 4게시글 좋아요 누르기 
		//boardDao.deleteLikeBoard(1, 4); //NY-10. 1이 4게시글 좋아요 취소하기 
		//System.out.println(boardDao.countLikeBoard(3)); // NY-11. 3 게시글 좋아요 조회 
		
//		System.out.println("N-12. 그 해당 게시글의 포함된 장소 출력");
//		System.out.print("게시글 번호 : "); int bno = sc.nextInt();
//		System.out.print("해당 멤버 id : "); int memId = sc.nextInt();
//		System.out.print("페이지 번호 : "); int page = sc.nextInt();
//		List <PlaceDto> placeDtos = boardDao.getSelectedPlaces(bno, memId, page);// NY-12. (1이 1의 게시글을 보고 있을 경우)
//		String placeId = null;
//		for(int i=0; i<placeDtos.size(); i++) {
//			if(placeDtos.get(i).placeId.equals(placeId)) {
//				System.out.println("이미지 : " + placeDtos.get(i).img);
//			}else {
//				System.out.println();	
//				System.out.println("장소id : " + placeDtos.get(i).placeId);
//				System.out.println("장소이름 : " + placeDtos.get(i).name);
//				System.out.println("카테고리 : " + placeDtos.get(i).category);
//				System.out.println("주소 : " + placeDtos.get(i).address);
//				System.out.println("평균별점 : " + placeDtos.get(i).avgRating);
//				System.out.println("리뷰수 : " + placeDtos.get(i).reviewCnt);
//				System.out.println("찜 유무 : " + placeDtos.get(i).isLikedPlace);
//				System.out.println("이미지 : " + placeDtos.get(i).img);
//			}
//			placeId =  placeDtos.get(i).placeId;
//		}
		
//		// NY-13. (1이 1의 게시글을 보고 있을 경우)
//		int bno = 1;
//		int memId = 1;
//		String placeId = "ChIJFaft2p-NaDURpWUIUUDNxCE";
//		int page = 1;
//		List <PlaceDto> placeDtos = boardDao.getLikedPlaces(bno, memId, placeId, page);
//		String pId = null;
//		for(int i=0; i<placeDtos.size(); i++) {
//			if(placeDtos.get(i).placeId.equals(pId)) {
//				System.out.println("이미지 : " + placeDtos.get(i).img);
//			} else {
//				System.out.println();
//				System.out.println("장소id : " + placeDtos.get(i).placeId);
//				System.out.println("장소이름 : " + placeDtos.get(i).name);
//				System.out.println("카테고리 : " + placeDtos.get(i).category);
//				System.out.println("주소 : " + placeDtos.get(i).address);
//				System.out.println("평균별점 : " + placeDtos.get(i).avgRating);
//				System.out.println("리뷰수 : " + placeDtos.get(i).reviewCnt);
//				System.out.println("찜 유무 : " + placeDtos.get(i).isLikedPlace);
//				System.out.println("이미지 : " + placeDtos.get(i).img);
//			}
//			pId = placeDtos.get(i).placeId;
//		}
		
		//NY-14.
//		String title = "게시글";
//		int bno = 1;
//		boardDao.modifyTitle(title, bno);
		
		//NY-15.
//		int bno = 1;
//		int memId = 1;
//		String placeId = "ChIJFaft2p-NaDURpWUIUUDNxCE";
//		String content = "부산";
//		int page = 1;
//		
//		List <PlaceDto> placeDtos = boardDao.getSerchedPlace(memId, content, placeId, bno, page);
//		String pId = null;
//		for(int i=0; i<placeDtos.size(); i++) {
//			if(placeDtos.get(i).placeId.equals(pId)) {
//				System.out.println("이미지 : " + placeDtos.get(i).img);
//			} else {
//				System.out.println();
//				System.out.println("장소id : " + placeDtos.get(i).placeId);
//				System.out.println("장소이름 : " + placeDtos.get(i).name);
//				System.out.println("카테고리 : " + placeDtos.get(i).category);
//				System.out.println("주소 : " + placeDtos.get(i).address);
//				System.out.println("평균별점 : " + placeDtos.get(i).avgRating);
//				System.out.println("리뷰수 : " + placeDtos.get(i).reviewCnt);
//				System.out.println("찜 유무 : " + placeDtos.get(i).isLikedPlace);
//				System.out.println("이미지 : " + placeDtos.get(i).img);
//			}
//			pId = placeDtos.get(i).placeId;
//		}

		//NY-16.
//		int bno = 2;
//		boardDao.createShareKey(bno);
		
		//NY-17.
//		int memId = 1;
//		int bno = 2;
//		String key = "zAR67j";
//		boardDao.addSharedMember(memId, bno, key);
		
		//NY-18.
//		int bno = 4;
//		String startDate = "20240401";
//		String endDate = "20240403";
//		boardDao.modifyTravelDate(bno, startDate, endDate);
		
		//NY-21.
//		int bno = 2;
//		boardDao.updateFinalDate(bno);
		
		//NY-22.
//		int memId = 1;
//		int bno = 1;
//		List<BoardInfoDto> list = boardDao.getBoardInfo(memId, bno);
//		//String pId = null;
//		if(list.size() > 0) {
//		    BoardInfoDto board = list.get(0);
//
//		    // 게시글 공통 정보 1번만 출력
//		    System.out.println("작성자 ID : " + board.writerId);
//		    System.out.println("게시글 제목 : " + board.title);
//		    System.out.println("출발지 ID : " + board.startPlaceId);
//		    System.out.println("도착지 ID : " + board.arrPlaceId);
//		    System.out.println("출발 날짜 : " + board.startDate);
//		    System.out.println("도착 날짜 : " + board.endDate);
//
//		    System.out.println("최대 경비 : " + board.maxCost);
//		    System.out.println("교통비 : " + board.transportCost);
//		    System.out.println("식비 : " + board.foodCost);
//		    System.out.println("숙소비 : " + board.roomCost);
//		    System.out.println("기타 경비 : " + board.etcCost);
//
//		    System.out.println("댓글 수 : " + board.commentCnt);
//		    System.out.println("찜 여부 : " + board.isLikeBoard);
//		    System.out.println("찜 개수 : " + board.likeBoardCnt);
//
//		    System.out.println("===== 블럭 정보 =====");
//
//		    // 블럭 정보만 반복 출력
//		    for(int i=0; i<list.size(); i++) {
//		        System.out.println("블럭 인덱스 : " + list.get(i).blockIdx);
//		        System.out.println("블럭 시작시간 : " + list.get(i).startTime);
//		        System.out.println("블럭 종료시간 : " + list.get(i).endTime);
//		        System.out.println("색상 코드 : " + list.get(i).colorCode);
//		        System.out.println("장소 이름 : " + list.get(i).name);
//		        System.out.println();
//		    }
//		}
//		else {
//		    System.out.println("조회된 게시글이 없습니다.");
//		}
//		
		//NY-24.
//		int bno = 1;
//		int memberId = 4;
//		String startPlaceId = "ChIJA3CU42aifDURaq-3csGXvuc";
//		String startDate = "20270404";
//		boardDao.copyBoard(bno, memberId, startPlaceId, startDate);
		
		// HA-2 a,b HA-3 a,b
//		int memberId = 1;
//		// HA-2 a
//		List<BoardDto> list = boardDao.showBoardsLatestOrder(memberId, 0);
//		// HA-2 b
//		List<BoardDto> list = boardDao.showBoardsLatestOrder(0);
		// HA-3 a
//		String key = "부산";
//		List<BoardDto> list = boardDao.showBoardsKeyOrder(key, memberId, 0);
		// HA-3 b
//		String key = "부산";
//		List<BoardDto> list = boardDao.showBoardsKeyOrder(key, 0);
//		int tempBno = -1;
//		for(int i=0;i<list.size();i++) {
//			BoardDto temp = list.get(i);
//			if(temp.bno == tempBno) {
//				System.out.println("위도, 경도: " + temp.lat + ", " + temp.lng);
//			}else {
//				System.out.println();
//				System.out.println("게시글 제목: " + temp.title);
//				System.out.println("여행년도, 월: " + temp.year + "년 " + temp.month + "월");
//				System.out.println("수정 후 경과: " + temp.elapsedTime);
//				System.out.println("찜수: " + temp.likedBoardCnt);
//				System.out.println("찜유무: " + (temp.isLikedBoard ? "❤️" : "🤍"));
//				System.out.println("위도, 경도: " + temp.lat + ", " + temp.lng);
//			}
//			tempBno = temp.bno;
//		}
		
		// HA-31 a b c d e
//		int bno = 100;
//		boardDao.modifyCostAi(bno, 1, 1, 1, 1, 1);
//		boardDao.modifyTransportCost(bno, 2);
//		boardDao.modifyFoodCost(bno, 2);
//		boardDao.modifyRoomCost(bno, 2);
//		boardDao.modifyetcCost(bno, 2);
		
		// HA-35
//		List<RecommendedPlacesDto> list = boardDao.viewRecommendedPlace();
//		for(int i=0;i<list.size();i++) {
//			RecommendedPlacesDto r = list.get(i);
//			System.out.println("추천장소ID: " + r.rcPlaceId);
//			System.out.println("이름: " + r.name);
//			System.out.println("위도, 경도: " + r.lat + ", " + r.lng);
//			System.out.println();
//		}
		
		// HA-36
//		int bno = 100;
//		boardDao.delBoard(bno);
	}

}
