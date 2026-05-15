package travel_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AiBlockDao {
	
	/** NY-25. AI 블럭 삭제(delete)  (p.16 일정표-메인(일 단위/AI확장)) - block
		input : 게시글의 인덱스(bno)
		output: -*/
	void deleteAiBlock(int bno) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "DELETE FROM ai_blocks WHERE bno= ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bno);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	/** NY-26. AI 블럭 삽입(insert)  (p.16 일정표-메인(일 단위/AI확장))
		input : 장소 id(place_id), 블럭 시작시간(start_time), 블럭 마지막 시간(ai_end_time), 앞과의 여행 거리 시간(ai_tavel_time)
		output: -*/
	void insertAiBlock(int bno, String placeId, String startTime, String endTime, int travelTime) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "INSERT INTO ai_blocks(ai_block_idx, bno, place_id, ai_start_time, ai_end_time, ai_travel_time) "
				+ "VALUES(SEQ_AI_BLOCK.nextval, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ? ) ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bno);
		pstmt.setString(2,placeId);
		pstmt.setString(3,startTime);
		pstmt.setString(4,endTime);
		pstmt.setInt(5,travelTime);
		
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	/** NY-27. AI 블록 복제  (p.16 일정표-메인(일 단위/AI확장))
		input: 해당 게시글 인덱스(bno)
		output: - */
	void copyAiBlock(int bno) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "INSERT INTO blocks(block_idx, bno, place_id, start_time, end_time, checked_ai, travel_time, color_idx) "
				+ "SELECT SEQ_BLOCK.nextval, ?, place_id, ai_start_time, ai_end_time, 0, ai_travel_time, 1 "
				+ "FROM ai_blocks "
				+ "WHERE bno = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bno);
		pstmt.setInt(2,bno);
		
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	/** HA-37 AI추천 블록 조회 (p.16 일정표-메인(일 단위/AI확장))
		input : 글번호(bno)
		output : ArrayList<AiBlocksDto> 
		이동시간이 만약 null일 경우 -1 리턴*/
	List <AiBlocksDto> getAiBlock(int bno) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "SELECT \r\n"
				+ "    ab.ai_block_idx \"블럭 인덱스\", "
				+ "    ab.bno \"게시글 번호\", "
				+ "    ab.place_id \"장소id\", "
				+ "    ab.ai_start_time \"시작 시간\", "
				+ "    ab.ai_end_time \"끝 시간\", "
				+ "    ab.ai_travel_time \"이동 시간\", "
				+ "    p.name \"장소 이름\", "
				+ "    p.lat \"위도\", "
				+ "    p.lng \"경도\" "
				+ "FROM ai_blocks ab "
				+ "    INNER JOIN places p ON p.place_id = ab.place_id "
				+ "WHERE bno = ? "
				+ "ORDER BY ai_start_time";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bno);
		
		// 3. 결과테이블 : “ResultSet객체”
		ResultSet rs = pstmt.executeQuery();
		List<AiBlocksDto> aiBlocksDtos = new ArrayList<>();
		while (rs.next()) {
			int aiBlockIdx = rs.getInt("블럭 인덱스");
			int bno1 = rs.getInt("게시글 번호");
			String placeId = rs.getString("장소id");
			String aiStartTime = rs.getString("시작 시간");
			String aiEndTime = rs.getString("끝 시간");
			//int aiTravelTime = rs.getInt("이동 시간");
			int aiTravelTime = (rs.getString("이동 시간")==null?-1:rs.getInt("이동 시간"));
			String name = rs.getString("장소 이름");
			double lat = rs.getDouble("위도");
			double lng = rs.getDouble("경도");
			AiBlocksDto aiBlocksDto = new AiBlocksDto(aiBlockIdx, bno1, placeId, aiStartTime, aiEndTime, aiTravelTime, name, lat, lng);
			aiBlocksDtos.add(aiBlocksDto);
		}
		rs.close();
		pstmt.close();
		conn.close();
		
		return aiBlocksDtos;
	}

	public static void main(String[] args) throws Exception {
		AiBlockDao a = new AiBlockDao();
		Scanner sc = new Scanner(System.in);
		
		//NY-25.
		//a.deleteAiBlock(4);
		
		//NY-26.
//		int bno = 2;
//		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
//		String startTime = "2026-03-21 14:00:00";
//		String endTime = "2026-03-21 14:00:00";
//		int travelTime = 10;
//		a.insertAiBlock(bno, placeId, startTime, endTime, travelTime);

		//NY-27.
//		int bno = 2;
//		a.copyAiBlock(bno);
		
		//HA-37.
		System.out.print("ai 블럭 조회할 게시글 번호 : ");
		int bno = sc.nextInt();
		List<AiBlocksDto> list = a.getAiBlock(bno);
		for(int i=0; i<list.size(); i++) {
			System.out.println();
			System.out.println("블럭 인덱스 : " + list.get(i).aiBlockIdx);
			System.out.println("게시글 번호 : " + list.get(i).bno);
			System.out.println("장소 id : " + list.get(i).placeId);
			System.out.println("시작 시간 : " + list.get(i).aiStartTime);
			System.out.println("끝 시간 : " + list.get(i).aiEndTime);
			System.out.println("이동 시간 : " + list.get(i).aiTravelTime);
			System.out.println("장소 이름 : " + list.get(i).name);
			System.out.println("위도 : " + list.get(i).lat);
			System.out.println("경도 : " + list.get(i).lng);
		}
		
	}

}
