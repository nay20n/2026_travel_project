package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
					likedCnt == 1 ? true : false);
			list.add(p);
		}

		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}

	/**
	*/
	public static void main(String[] args) throws Exception {
		PlaceDao p = new PlaceDao();
		String placeId = "ChIJzzlcLQGifDURm_JbQKHsEX4";
		System.out.println(p.viewPlaceDetails(placeId, 1).get(0).webUrl == null);
		System.out.println("정상종료");
	}

}
