package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberDao {
	
	/** HA-1. 프로필 이미지 조회 (p.2 / 메인페이지)
		input : member_id (회원 아이디)
		output : profile_img(프로필 이미지) 파일명 */
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
	
	/** 이메일, 비밀번호 조회 (p.3 / 로그인+간편가입 팝업, p.5 / 정보수정1)
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
		// 6자리 소문자, 대문자, 숫자 랜덤 key 생성
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
	
	/** HA-7  인증코드, 만료일시 조회 (p.5 / 정보수정1)
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
		input : 이메일(email)  
		output : - */
	void addMember(String email) throws Exception {
		// 16자리 소문자, 대문자, 숫자 랜덤 key 생성
		StringBuilder sb = new StringBuilder();
		while (sb.length() < 16) {
			int temp = (int) (Math.random() * 75) + 48;
			if (temp < 58 || (temp > 64 && temp < 91) || (temp > 96))
				sb.append((char) temp);
		}
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
		
		String sql = "INSERT INTO members(member_id, email, nick_name, pw)"
				+ " VALUES(seq_member.nextval, ?, '익명의 사용자', ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		pstmt.setString(2, sb.toString());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	/** HA-9 프로필이미지 수정 (p.6 / 정보수정2)
		input : 아이디(memberId), 새프로필이미지(profileImg)  
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
	
	/** HA-10닉네임 수정 (p.6 / 정보수정2)
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
		input : 아이디(memberId), 새이메일(email)  
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
		input : 아이디(memberId), 새비밀번호(pw)  
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
	
	/** NY-1. 내 게시글들 조회, 공동작업자부분도 띄어줘야함 (p.4 / 마이페이지)
		input : 로그인한 사람의 member_id, 페이지번호
		output : 내 일정들 나열 (List<MypageDto>) */
	List<Map<String,Object>> getMyBoard(int memberId, int page) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
	
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
	
		String sql = "SELECT b.bno \"게시글 번호\", TRUNC(SYSDATE-start_date) \"디데이\", title \"일정 이름\", nick_name \"작성자\", "
				+ "(SELECT COUNT(*) FROM like_boards lb WHERE lb.member_id = ? AND lb.bno = b.bno)\"찜 유무\", "
				+ "(SELECT COUNT(*)FROM like_boards lb WHERE lb.bno = b.bno) \"찜 개수\" "
				+ "FROM members m "
				+ "INNER JOIN boards b ON m.member_id = b.writer_id "
				+ "WHERE b.writer_id = ? OR b.share_user_id = ? "
				+ "ORDER BY final_date DESC "; 
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setInt(2, memberId);
		pstmt.setInt(3, memberId);
	
		ResultSet rs = pstmt.executeQuery();
		List<Map<String,Object>> mypageDtos = new ArrayList<>();
	    while (rs.next()) { 
	    	Map<String,Object> tempMap = new HashMap<>();
	    	tempMap.put("bno", rs.getInt("게시글 번호"));
	    	tempMap.put("dDayInt", rs.getInt("디데이"));
	    	int dDayInt = rs.getInt("디데이");
	    	String dDay = "";
	    	if(dDayInt > 0) {
	    		dDay = "D+" + dDayInt;
	    	} else if (dDayInt < 0 ) {
	    		dDay = "D-" + Math.abs(dDayInt);
	    	} else { dDay = "D-day"; }
	    	tempMap.put("dDay", dDay);
	    	tempMap.put("title", rs.getString("일정 이름"));
	    	tempMap.put("nickname", rs.getString("작성자"));
	    	tempMap.put("isLiked", rs.getInt("찜 유무")==1);
	    	tempMap.put("likeCnt", rs.getInt("찜 개수"));
	    	mypageDtos.add(tempMap);
	    }
	
	    rs.close();
		pstmt.close();
		conn.close();
		
		return mypageDtos;
	}
	
	/** NY-2. 내가 찜한 일정들 조회 (p.4 / 마이페이지)
		input : 로그인한 사람의 member_id, 페이지 번호 
		output : 로그인한 사람이 찜한 게시물 조회 (List<MyPageDto>) */
	List<Map<String,Object>> getLikedBoard(int memberId, int page) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
	
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
	
		String sql = "SELECT  b.bno \"찜한 게시글 번호\", title \"일정이름\", nick_name \"작성자\" , "
				+ "(SELECT COUNT(*) FROM like_boards lb WHERE lb.member_id = ? AND lb.bno = b.bno)\"찜 유무\", "
				+ "(SELECT COUNT(*) FROM like_boards lb WHERE lb.bno = b.bno) \"찜 개수\" "
				+ "FROM like_boards lb "
				+ "INNER JOIN boards b ON lb.bno = b.bno "
				+ "INNER JOIN members m ON b.writer_id = m.member_id "
				+ "WHERE lb.member_id = ? "
				+ "ORDER BY like_time DESC " ;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setInt(2, memberId);
	
		ResultSet rs = pstmt.executeQuery();
		List<Map<String,Object>> mypageDtos = new ArrayList<>();
	    while (rs.next()) {
	    	Map<String,Object> tempMap = new HashMap<>();
	    	tempMap.put("bno", rs.getInt("찜한 게시글 번호"));
	    	tempMap.put("title", rs.getString("일정이름"));
	    	tempMap.put("nickname", rs.getString("작성자"));
	    	tempMap.put("isLiked", rs.getInt("찜 유무")==1);
	    	tempMap.put("likeCnt", rs.getInt("찜 개수"));
	    	mypageDtos.add(tempMap);
	    }
	
	    rs.close();
		pstmt.close();
		conn.close();
		
		return mypageDtos;
	}

	/** NY-3. 내가 댓글 단 일정 조회 (p.4 / 마이페이지) 
		input : 로그인한 사람의 member_id, 페이지 번호
		output : 로그인한 사람이 댓글을 단 게시물 조회  (List<MyPageDto>) */
	List<Map<String,Object>> getCommentBoard(int memberId, int page) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
	
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
	
		String sql = "SELECT b.bno \"댓글을 단 게시글 번호\","
				+ " title \"일정이름\", "
				+ "nick_name \"작성자\", "
				+ "(SELECT COUNT(*) FROM like_boards lb WHERE lb.member_id = ? AND lb.bno = b.bno) \"찜 유무\", "
				+ "(SELECT COUNT(*) FROM like_boards lb WHERE lb.bno = b.bno) \"찜 개수\" "
				+ "FROM members m "
				+ "INNER JOIN boards b ON m.member_id = b.writer_id "
				+ "WHERE (SELECT MAX(c.final_date) FROM comments c WHERE c.bno = b.bno  AND writer_id = ?) IS NOT NULL "
				+ "ORDER BY (SELECT MAX(c.final_date) FROM comments c WHERE c.bno = b.bno  AND writer_id = ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setInt(2, memberId);
		pstmt.setInt(3, memberId);
	
		ResultSet rs = pstmt.executeQuery();
		List<Map<String,Object>> mypageDtos = new ArrayList<>();
	    while (rs.next()) {
	    	Map<String,Object> tempMap = new HashMap<>();
	    	tempMap.put("bno", rs.getInt("댓글을 단 게시글 번호"));
	    	tempMap.put("title", rs.getString("일정이름"));
	    	tempMap.put("nickname", rs.getString("작성자"));
	    	tempMap.put("isLiked", rs.getInt("찜 유무")==1);
	    	tempMap.put("likeCnt", rs.getInt("찜 개수"));
	    	mypageDtos.add(tempMap);
	    }
	
	    rs.close();
		pstmt.close();
		conn.close();
		
		return mypageDtos;
	}
	
	/** NY-4. 프로필 & 활동한 내용 카운트 조회 (p.4 / 마이페이지) -member
		ProfileDto(프로필 사진, 닉네임, 작성한 일정 수, 찜한 수, 댓글 쓴 수)
		input : 로그인한 사람의 member_id
		output : ProfileDto*/
	ProfileDto getMemberProfile(int memberId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
	
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
	
		String sql = "SELECT profile_img \"프로필 사진\", nick_name \"닉네임\", "
				+ "	(SELECT COUNT(*) FROM boards WHERE writer_id = ? OR share_user_id = ?) \"내 일정 수\", "
				+ "	(SELECT COUNT(*) FROM like_boards WHERE member_id = ?) \"찜한 수\", "
				+ "(SELECT COUNT(*) FROM comments WHERE writer_id = ?) \"댓글 쓴 수\" "
				+ "FROM members "
				+ "WHERE member_id = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, memberId);
		pstmt.setInt(2, memberId);
		pstmt.setInt(3, memberId);
		pstmt.setInt(4, memberId);
		pstmt.setInt(5, memberId);
	
		//3. 결과테이블 : “ResultSet객체”
		ResultSet rs = pstmt.executeQuery();
		ProfileDto profileDto = null;
	    if (rs.next()) { 
	    	
	    	String profile = rs.getString("프로필 사진");
	    	String nickName = rs.getString("닉네임");
	    	int myTavelCnt = rs.getInt("내 일정 수");
	    	int myLikeCnt = rs.getInt("찜한 수");
	    	int myCommentCnt = rs.getInt("댓글 쓴 수");
	    	
	    	profileDto = new ProfileDto(profile, nickName, myTavelCnt, myLikeCnt, myCommentCnt);
	    }
	
	    rs.close();
		pstmt.close();
		conn.close();
		
		return profileDto;
	}

	public static void main(String[] args) throws Exception {
		MemberDao a = new MemberDao();
		
		//NY-1.
//		int bno = 1;
//		int page = 1;
//		List <MypageDto> list = a.getMyBoard(bno, page);
//		for(int i=0; i<list.size(); i++) {
//			System.out.println("게시글 번호 : " + list.get(i).bno);
//			System.out.println("디데이 : " + list.get(i).dDay);
//			System.out.println("일정 이름 : " + list.get(i).title);
//			System.out.println("작성자 : " + list.get(i).writerNick);
//			System.out.println("찜 유무 : " + list.get(i).isLiked);
//			System.out.println("찜 개수 : " + list.get(i).likeBoardCnt);
//			System.out.println();
//		}
		
		//NY-2.
//		int bno = 1;
//		int page = 1;
//		List <MypageDto> list = a.getLikedBoard(bno, page);
//		for(int i=0; i<list.size(); i++) {
//			System.out.println("게시글 번호 : " + list.get(i).bno);
//			System.out.println("디데이 : " + list.get(i).dDay);
//			System.out.println("일정 이름 : " + list.get(i).title);
//			System.out.println("작성자 : " + list.get(i).writerNick);
//			System.out.println("찜 유무 : " + list.get(i).isLiked);
//			System.out.println("찜 개수 : " + list.get(i).likeBoardCnt);
//			System.out.println();
//		}
		
		//NY-3.
//		int bno = 1;
//		int page = 1;
//		List <MypageDto> list = a.getCommentBoard(bno, page);
//		for(int i=0; i<list.size(); i++) {
//			System.out.println("게시글 번호 : " + list.get(i).bno);
//			System.out.println("디데이 : " + list.get(i).dDay);
//			System.out.println("일정 이름 : " + list.get(i).title);
//			System.out.println("작성자 : " + list.get(i).writerNick);
//			System.out.println("찜 유무 : " + list.get(i).isLiked);
//			System.out.println("찜 개수 : " + list.get(i).likeBoardCnt);
//			System.out.println();
//		}
		
		//NY-4.
//		int memberId = 1;
//		ProfileDto profileDto = a.getMemberProfile(memberId);
//		System.out.println("프로필 사진 : " + profileDto.profile);
//		System.out.println("닉네임 : " + profileDto.nickName);
//		System.out.println("내 일정 수 : " + profileDto.myTavelCnt);
//		System.out.println("찜한 수 : " + profileDto.myLikeCnt);
//		System.out.println("댓글 쓴 수 : " + profileDto.myCommentCnt);
		
		System.out.println();
		System.out.println("정상종료");
	}

}
