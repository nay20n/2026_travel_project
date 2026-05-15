package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaceDao {
	
	/** HA-13 장소정보 조회 (p.12 / 정보창팝업1)
		input : 장소아이디(placeId) , 회원아이디(memberId) 
		output : List<PlaceInfoDto> */
	List<PlaceInfoDto> viewPlaceDetails(String placeId, int memberId) throws Exception {
		List<PlaceInfoDto> list = new ArrayList<>();

		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT p.name 장소이름,"
				+ " (SELECT NVL(AVG(rating),0) FROM reviews r WHERE p.place_id = r.place_id) 평균별점,"
				+ " p.category 분류, (SELECT COUNT(*) FROM reviews r WHERE p.place_id = r.place_id) 리뷰수, pi.image 이미지,"
				+ " p.address 주소, p.business_hours 영업시간, p.website_url 공식사이트, (SELECT COUNT(*) FROM like_places WHERE member_id = ? AND place_id = ?) 찜한유무"
				+ " FROM places p LEFT OUTER JOIN places_images pi" + " ON p.place_id = pi.place_id"
				+ " WHERE p.place_id = ?" 
				+ " ORDER BY pi.image_num";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setString(2, placeId);
		pstmt.setString(3, placeId);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString("장소이름");
			double avgRating = rs.getDouble("평균별점");
			String category = rs.getString("분류");
			int reviewCnt = rs.getInt("리뷰수");
			String address = rs.getString("주소");
			String businessHours = rs.getString("영업시간");
			String webUrl = rs.getString("공식사이트");
			String img = rs.getString("이미지");
			int likedCnt = rs.getInt("찜한유무");
			PlaceInfoDto p = new PlaceInfoDto(name, avgRating, reviewCnt, category, address, businessHours, webUrl, img,
					likedCnt==1);
			list.add(p);
		}

		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}

	/** HA-15 찜한장소 삽입 (p.12 일정표-정보창팝업1)
		input : 회원아이디(memberId), 장소아이디(placeId)
		output : - */
	void addLikedPlace(int memberId, String placeId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "INSERT INTO like_places(member_id, place_id, like_time) VALUES(?,?,SYSDATE)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setString(2, placeId);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-16 찜한장소 삭제 (p.12 일정표-정보창팝업1)
		input : 회원아이디(member_id), 장소아이디(place_id)
		output : - */
	void delLikedPlace(int memberId, String placeId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "DELETE FROM like_places WHERE member_id = ? AND place_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setString(2, placeId);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-17 장소 댓글 목록 조회 (p.13 일정표-정보창팝업2) 
		input : 장소아이디(place_id)
		output : List<ReviewDto>
		결과가 공집합일 수 있음 */
	List<ReviewDto> viewReviews(String placeId) throws Exception {
		List<ReviewDto> list = new ArrayList<>();
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT r.content, r.rating, r.final_date, r.image,"
				+ " m.nick_name, m.profile_img"
				+ " FROM reviews r INNER JOIN members m"
				+ "	ON r.user_id = m.member_id"
				+ " WHERE place_id = ?"
				+ " ORDER BY r.final_date DESC";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, placeId);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			String content = rs.getString("content");
			int rating = rs.getInt("rating");
			String date = rs.getString("final_date");
			String reviewImg = rs.getString("image");
			String writerNick = rs.getString("nick_name");
			String writerProfile = rs.getString("profile_img");
			list.add(new ReviewDto(content, rating, date, reviewImg, writerNick, writerProfile));
		}
				
		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}
	
	/** HA-18 장소 댓글 삽입 (p.13 일정표-정보창팝업2)
		input : 회원아이디(memberId), 장소아이디(placeId), 내용(content), 별점(rating), 사진(image)
		output : - */
	void addPlaceReview(int memberId, String placeId, String content, int rating, String image) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "INSERT INTO reviews(review_idx, user_id, place_id, content, rating, final_date, image)"
				+ "VALUES(seq_reviews.nextval, ?, ?, ?, ?, SYSDATE, ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setString(2, placeId);
		pstmt.setString(3, content);
		pstmt.setInt(4, rating);
		pstmt.setString(5, image);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-19a 장소 댓글 삭제 (p.13 일정표-정보창팝업2)
		input : 리뷰번호(reviewIdx), 삭제하는 멤버(memberId)
		output : - */
	void delPlaceReview(int reviewIdx, int memberId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "DELETE FROM reviews WHERE review_idx = ? AND user_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, reviewIdx);
		pstmt.setInt(2, memberId);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-19b 장소 댓글 수정 (p.13 일정표-정보창팝업2)
		input : 리뷰번호(reviewIdx), 삭제하는 멤버(memberId), 새내용(content)
		output : - */
	void modifyPlaceReview(int reviewIdx, int memberId, String content) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "UPDATE reviews SET content = ? WHERE review_idx = ? AND user_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, content);
		pstmt.setInt(2, reviewIdx);
		pstmt.setInt(3, memberId);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** NY-6. 우리의 DB에 없는 장소 삽입  (p7 / 일정표만들기-출발지는?,p.8 일정표만들기-떠나고 싶은도시는?, p.11 / 일정표 - 메인(주)) 
		input : API에서 가져온 장소 정보들, 정보 사진들, 사진출력순서
		output: - */
	void addPlace(String placeId, String name, String category, String address, double lat, double lng, String website_url) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "INSERT INTO places (place_id, name, category, address,lat,lng,website_url) "
				+ "VALUES (?,?,?,?,?,?,?) ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, placeId);
		pstmt.setString(2, name);
		pstmt.setString(3, category);
		pstmt.setString(4, address);
		pstmt.setDouble(5, lat);
		pstmt.setDouble(6, lng);
		pstmt.setString(7, website_url);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** NY-7. 우리의 DB에 있는지 확인(p7 / 일정표만들기-출발지는?,p.8 일정표만들기-떠나고 싶은도시는?, p.11 / 일정표 - 메인(주))
	input : 장소 (place_id)
	output : true 존재 O, false 존재 X */
	boolean isPlaceExists(String placeId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
	
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
	
		String sql = "SELECT COUNT(*) \"장소 DB 존재\" FROM places WHERE place_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, placeId);
	
		ResultSet rs = pstmt.executeQuery();
		boolean isExisted = false;
		if (rs.next()) {
			isExisted = rs.getInt("장소 DB 존재") == 1 ? true : false;
		}
				
		rs.close();
		pstmt.close();
		conn.close();
		return isExisted;
	}
	
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		PlaceDao p = new PlaceDao();
		
		// 기본 설정값
		//String placeId = "ChIJe1QMOBvraDUR9__2s01MxDE";
		int memberId = 100;
		String image = "im.png";
		
		// HA-13
//		List<PlaceInfoDto> list = p.viewPlaceDetails(placeId, memberId);
//		PlaceInfoDto pl;
//		for(int i=0;i<list.size();i++){
//			pl = list.get(i);
//			System.out.println("이미지: " + pl.img);
//		}
//		pl = list.get(0);
//		System.out.println("장소 이름: " + pl.name);
//		System.out.println("평균 별점: " + (int)(pl.avgRating*10)/10.0);
//		System.out.println("리뷰수: " + pl.reviewCnt);
//		System.out.println("카테고리: " + pl.category);
//		if(pl.isLikedPlace) System.out.println("❤️");
//		else System.out.println("🤍");
//		System.out.println("주소: " + pl.address);
//		System.out.println("영업시간: " + pl.businessHours);
//		System.out.println("사이트: " + pl.webUrl);
		
		// HA-16
//		p.delLikedPlace(memberId, placeId);
//		System.out.println();
		
		// HA-17
//		List<ReviewDto> l = p.viewReviews(placeId);
//		for(int i=0;i<l.size();i++) {
//			ReviewDto r = l.get(i);
//			System.out.println("프로필 이미지: " + r.writerProfile);
//			System.out.println("작성자: " + r.writerNick);
//			System.out.println("별점: " + r.rating);
//			System.out.println("이미지: " + r.reviewImg);
//			System.out.println("댓글 내용: " + r.content);
//			System.out.println();
//		}
		
		// HA-18
//		System.out.print("댓글 내용: ");
//		String content = sc.nextLine();
//		System.out.print("별점(0~5 정수): ");
//		int rating = sc.nextInt();
//		sc.nextLine();
//		p.addPlaceReview(memberId, placeId, content, rating, image);
		
		// HA-19a
//		System.out.print("삭제할 댓글 인덱스: ");
//		int reviewIdx = sc.nextInt();
//		sc.nextLine();
//		p.delPlaceReview(reviewIdx, memberId);
		
		// HA-19b
//		System.out.print("수정할 댓글 인덱스: ");
//		int reviewIdx = sc.nextInt();
//		sc.nextLine();
//		System.out.print("댓글 내용: ");
//		String content = sc.nextLine();
//		p.modifyPlaceReview(reviewIdx, memberId, content);
		
		// NY-6.
//		String placeId = "test";
//		String name = "테스트";
//		String categort = "테스트";
//		String address = "주소";
//		double lat = 3.0;
//		double lng = 3.4909;
//		String website_url = "http: ";
//		p.addPlace(placeId, name, categort, address, lat, lng, website_url);
		
		// NY-7.
		String placeId = "ChIJe1QMOBvraDUR9__2s01MxDE";
		System.out.println("DB 존재 : " + p.isPlaceExists(placeId));
		
		// 정상종료
		sc.close();
		System.out.println("정상종료");
	}

}
