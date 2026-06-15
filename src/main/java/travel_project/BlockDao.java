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

public class BlockDao {
	/** HA-20 블록 삽입 (p.11 / 일정표-메인(주 단위))
		input : 게시글번호(bno), 시작시간(startTime), 끝시간(endTime) 
		output : 블록인덱스(blockIdx)
		주의 : -1 이 돌아왔다면 문제발생*/
	int addBlock(int bno, String startTime, String endTime) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "INSERT INTO blocks(block_idx, bno, start_time, end_time, checked_ai, color_idx)"
				+ " VALUES(seq_block.nextval, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), '0', 1)";
		PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"block_idx"});
		pstmt.setInt(1, bno);
		pstmt.setString(2, startTime);
		pstmt.setString(3, endTime);
		
		pstmt.executeUpdate();
		
		ResultSet rs = pstmt.getGeneratedKeys();
		int blockIdx = -1;
		if(rs.next()) blockIdx = rs.getInt(1);
		
		rs.close();
		pstmt.close();
		conn.close();
		return blockIdx;
	}
	
	/** HA-21 블록 장소 수정 (p.11 / 일정표-메인(주 단위))
		input : 블록인덱스(blockIdx), 장소아이디(placeId)
		output : - */
	void addBlockPlace(int blockIdx, String placeId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "UPDATE  blocks SET place_id = ? WHERE block_idx = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, placeId);
		pstmt.setInt(2, blockIdx);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-22 블록 삭제 (p.11 / 일정표-메인(주 단위))
		a. 인덱스번호로 삭제
		input : 블록인덱스(block_idx)
		output : - */
	void delBlock(int blockIdx) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "DELETE FROM blocks WHERE block_idx = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, blockIdx);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-22 블록 삭제 (p.11 / 일정표-메인(주 단위))
		b. 해당 게시글 전체 블록 삭제
		input : 글번호(bno), 작성자(memberId)
		output : - */
	void delBlock(int bno, int memberId) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "DELETE FROM (SELECT * FROM blocks bl INNER JOIN boards bo ON bl.bno = bo.bno WHERE bo.bno = ? AND writer_id = ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);
		pstmt.setInt(2, memberId);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-23 블록 색깔 수정 (p.11 / 일정표-메인(주 단위))
		input : 블록인덱스(blockIdx), 색 인덱스(colorIdx)
		output : - */
	void modifyBlockColor(int blockIdx, int colorIdx) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE  blocks SET color_idx = ? WHERE block_idx = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, colorIdx);
		pstmt.setInt(2, blockIdx);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-24 블록 시간 수정 (p.11 / 일정표-메인(주 단위))
		input : 블록인덱스(blockIdx), 시작시간(StartTime), 끝시간(endTime)  
		output : - */
	void modifyBlockTime(int blockIdx, String startTime, String endTime) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE  blocks SET start_time = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), end_time = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')" 
				+ " WHERE block_idx = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, startTime);
		pstmt.setString(2, endTime);
		pstmt.setInt(3, blockIdx);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-25 블록 AI 반영여부 수정 (p.11 / 일정표-메인(주 단위))
		input : 블록인덱스(blockIdx), 반영여부(isCheckedAi)
		output : - */
	void modifyBlockCheckedAi(int blockIdx, boolean isCheckedAi) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "UPDATE  blocks SET checked_ai = ? WHERE block_idx = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, isCheckedAi ? 1 : 0);
		pstmt.setInt(2, blockIdx);

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
	}
	
	/** HA-26 블록 팝업창 정보 조회 (p.11 / 일정표-메인(주 단위))
		input : 블록인덱스(blockIdx) 
		output : 해당 블록의 BlockDto */
	Map<String,Object> viewBlockDetails(int blockIdx) throws Exception {
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT b.start_time, b.end_time, b.checked_ai, "
				+ "b.color_idx, c.color_code, p.name, p.category, p.address, p.lat, p.lng "
				+ "FROM blocks b LEFT OUTER JOIN places p "
				+ "ON b.place_id = p.place_id "
				+ "INNER JOIN colors c ON b.color_idx = c.color_idx "
				+ "WHERE block_idx = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, blockIdx);
		
		Map<String, Object> map = new HashMap();
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			map.put("startTime", rs.getString("start_time"));
			map.put("endTime", rs.getString("end_time"));
			map.put("isCheckedAi", rs.getInt("checked_ai")==1);
			map.put("colorIdx", rs.getInt("color_idx"));
			map.put("colorCode",rs.getString("color_code") );
			map.put("name", rs.getString("name"));
			map.put("category", rs.getString("category"));
			map.put("address", rs.getString("address"));
			map.put("lat", rs.getDouble("lat"));
			map.put("lng", rs.getDouble("lng"));
		}
				
		rs.close();
		pstmt.close();
		conn.close();
		return map;
	}
	
	/** HA-27 게시글의 블록 정보 전체 조회 (p.11 / 일정표-메인(주 단위))
		input : 게시글번호
		output : List Map<String, Object> 
		주의 : 장소가 없는 경우 위도, 경도에 0이 들어감 */
	List<Map<String, Object>> showAllBlocks(int bno) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT b.block_idx, b.start_time, b.end_time, b.checked_ai, b.travel_time, "
				+ "b.color_idx, c.color_code, p.name, lat, lng "
				+ "FROM blocks b LEFT OUTER JOIN places p ON b.place_id = p.place_id "
				+ "INNER JOIN colors c ON b.color_idx = c.color_idx "
				+ "WHERE b.bno = ? "
				+ "ORDER BY start_time ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);

		List<Map<String,Object>> mapList = new ArrayList<>();
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("blockIdx", rs.getInt("block_idx"));
			tempMap.put("startTime", rs.getString("start_time"));
			tempMap.put("endTime", rs.getString("end_time"));
			tempMap.put("isCheckedAi", rs.getInt("checked_ai")==1);
			tempMap.put("travelTime", rs.getObject("travel_time",Integer.class));
			tempMap.put("colorIdx", rs.getInt("color_idx"));
			tempMap.put("colorCode", rs.getString("color_code"));
			tempMap.put("name", rs.getString("name"));
			tempMap.put("lat", rs.getDouble("lat"));
			tempMap.put("lng", rs.getDouble("lng"));
			
			mapList.add(tempMap);
		}
		rs.close();
		pstmt.close();
		conn.close();

		return mapList;
	}
	
	/** HA-28 게시글의 블록 정보 특정 시간 사이 조회 (p.11 / 일정표-메인(주 단위))
		input : 게시글번호(bno), 시작시간(inputStartTime), 끝시간(inputEndTime)  
		output : ArrayList<BlockDto> 
		주의 : 장소가 없는 경우 위도, 경도에 0이 들어감*/
	List<Map<String,Object>> showBlocksBetween(int bno, String inputStartTime, String inputEndTime) throws Exception {
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);

		String sql = "SELECT block_idx, start_time, end_time, checked_ai, travel_time, "
				+ "b.color_idx, c.color_code, p.name, lat, lng "
				+ "FROM blocks b LEFT OUTER JOIN places p "
				+ "ON b.place_id = p.place_id "
				+ "INNER JOIN colors c ON b.color_idx = c.color_idx "
				+ "WHERE b.bno = ? "
				+ "AND start_time BETWEEN TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') "
				+ "ORDER BY start_time ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);
		pstmt.setString(2, inputStartTime);
		pstmt.setString(3, inputEndTime);

		ResultSet rs = pstmt.executeQuery();
		List<Map<String,Object>> list = new ArrayList<>();
		while(rs.next()) {
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("blockIdx", rs.getInt("block_idx"));
			tempMap.put("startTime", rs.getString("start_time"));
			tempMap.put("endTime", rs.getString("end_time"));
			tempMap.put("isCheckedAi", rs.getInt("checked_ai")==1);
			tempMap.put("travelTime", rs.getObject("travel_time",Integer.class));
			tempMap.put("colorIdx", rs.getInt("color_idx"));
			tempMap.put("colorCode", rs.getString("color_code"));
			tempMap.put("name", rs.getString("name"));
			tempMap.put("lat", rs.getDouble("lat"));
			tempMap.put("lng", rs.getDouble("lng"));
			
			list.add(tempMap);
		}
				
		rs.close();
		pstmt.close();
		conn.close();
		return list;
	}
	
	/** HA-29 블록 이동시간 수정 (p.11 / 일정표-메인(주 단위))
		input : 블록인덱스(blockIdx), 이동시간(travelTime) (분number) 
		output : - */
	void modifyBlockTravelTime(int blockIdx, int travelTime) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "UPDATE blocks SET travel_time = ? WHERE block_idx = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, travelTime);
		pstmt.setInt(2, blockIdx);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/** HA-38 하루 치 기존 일정 삭제 (p.16 일정표-메인(일 단위/AI확장))
		input : bno(수정하려는 글번호), date(지우려는 날짜 "YYYY-MM-DD")
		output : - */
	void delBlockInDate(int bno, String date) throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "DELETE FROM blocks WHERE bno = ? AND trunc(start_time) = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bno);
		pstmt.setString(2, date);
				
		pstmt.executeUpdate();
				
		pstmt.close();
		conn.close();
	}
	
	/**NY-23. 블록 색깔 변경 팔레트 조회 (p. 11 / 일정표 - 메인(주단위)) - block
	input: -
	output: List<String> 색깔 코드들  */
	List<String> getColors() throws Exception{
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "SELECT color_idx \"색깔순서\", color_name \"색깔이름\", color_code \"색깔코드\" FROM colors ORDER BY color_idx";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		ResultSet rs = pstmt.executeQuery();
		List<String> colorCodes = new ArrayList<>();
		while(rs.next()) {
			String colorCode = rs.getString("색깔코드");
			colorCodes.add(colorCode);
		}
		rs.close();
		pstmt.close();
		conn.close();
		
		return colorCodes;
	}
	/** NY–24-b. 게시글 전체 블럭 복사, 복제할 게시글 블럭 마다 반복
		input : 새 게시글 번호(newBno), 복제하고 싶은 게시글 번호(bno), 출발일(newStartTime)
		ouput: - 
		주의 : 날짜 형식 'YYYY-MM-DD'*/
	void copyBlock(int newBno, int bno, String newStartTime) throws Exception{
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String dbId = "ab";
		String dbPw = "12345";
				
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, dbId, dbPw);
				
		String sql = "INSERT INTO blocks(block_idx, bno, place_id, start_time, end_time, checked_ai, color_idx) "
				+ "SELECT "
				+ "	SEQ_BLOCK.NEXTVAL, "
				+ "?, place_id,"
				+ "start_time + (TO_DATE(?, 'YYYY-MM-DD') - TRUNC(start_time)), "
				+ "end_time + (TO_DATE(?, 'YYYY-MM-DD')  - TRUNC(end_time)), "
				+ "0, "
				+ "color_idx "
				+ "FROM blocks "
				+ "WHERE bno = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,newBno);
		pstmt.setString(2, newStartTime);
		pstmt.setString(3, newStartTime);
		pstmt.setInt(4,bno);
		
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
	}
	
	public static void main(String[] args) throws Exception {
		BlockDao b = new BlockDao();
		Scanner sc = new Scanner(System.in);
		
		// 기본 설정값
		int bno = 1;
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		
		// HA-20
//		String startTime = "2026-03-21 14:00:00";
//		String endTime = "2026-03-21 15:00:00";
//		System.out.println(b.addBlock(bno, startTime, endTime));
		
		// HA-21
//		System.out.print("변경할 블록 인덱스: ");
//		int blockIdx = sc.nextInt();
//		sc.nextLine();
//		b.addBlockPlace(blockIdx, placeId);
		
		// HA-22
		// a
//		System.out.print("변경할 블록 인덱스: ");
//		int blockIdx = sc.nextInt();
//		sc.nextLine();
//		b.delBlock(blockIdx);
		// b
//		b.delBlock(bno, 100);
		
		// HA-23
//		System.out.print("변경할 블록 인덱스: ");
//		int blockIdx = sc.nextInt();
//		sc.nextLine();
//		System.out.print("색 인덱스: ");
//		int colorIdx = sc.nextInt();
//		sc.nextLine();
//		b.modifyBlockColor(blockIdx, colorIdx);
		
		// HA-24
//		System.out.print("변경할 블록 인덱스: ");
//		int blockIdx = sc.nextInt();
//		sc.nextLine();
//		String startTime = "2026-03-22 14:00:00";
//		String endTime = "2026-03-24 15:00:00";
//		b.modifyBlockTime(blockIdx, startTime, endTime);
		
		// HA-25
//		System.out.print("변경할 블록 인덱스: ");
//		int blockIdx = sc.nextInt();
//		sc.nextLine();
//		b.modifyBlockCheckedAi(blockIdx, true);
		
		// HA-26
//		System.out.print("조회할 블록 인덱스: ");
//		int blockIdx = sc.nextInt();
//		sc.nextLine();
//		Map<String, Object> bl = b.viewBlockDetails(blockIdx);
////		System.out.println(bl.get("isCheckedAi").getClass());
//		System.out.println(bl);
		
		// HA-27
//		System.out.print("블럭을 조회할 게시글 번호: ");
//		bno = sc.nextInt();
//		List<Map<String,Object>> bd = b.showAllBlocks(bno);
//		for(int i=0;i<bd.size();i++) {
//			System.out.println(bd.get(i));
//		}
		
		// HA-28
		String startTime = "2026-05-06";
		String endTime = "2027-05-09";
		List<Map<String,Object>> bds = b.showBlocksBetween(bno, startTime, endTime);
		for(int i=0;i<bds.size();i++) {	
			System.out.println(bds.get(i));
		}
		
//		// HA-29
//		System.out.print("변경할 블록 인덱스: ");
//		int blockIdx = sc.nextInt();
//		sc.nextLine();
//		System.out.print("이동 시간: ");
//		int travelTime = sc.nextInt();
//		sc.nextLine();
//		b.modifyBlockTravelTime(blockIdx, travelTime);
		
		// HA-38
//		String date = "2026-03-21";
//		b.delBlockInDate(bno, date);
		
		//NY-23.
//		List<String> list = b.getColors();
//		for(int i=0; i<list.size(); i++) {
//			System.out.println("색깔 코드 : " + list.get(i));
//		}
		
		//NY-24b.
//		b.copyBlock(100, 1, "2026-06-04");
		
		// 정상종료
		sc.close();
		System.out.println("정상종료");
	}

}
