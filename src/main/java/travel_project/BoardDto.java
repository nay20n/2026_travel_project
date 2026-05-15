package travel_project;

public class BoardDto {
	int bno; // 게시글 번호
	String title; // 게시글 제목
	int year; // 여행년도
	int month; // 여행월
	String elapsedTime; // 수정 후 경과시간 ex.6분전, 1시간전, 3일전, 4월 17일
	int likedBoardCnt; // 찜 수
	boolean isLikedBoard; // 찜 유무
	double lat; // 위도
	double lng; // 경도

	BoardDto(int bno, String title, int year, int month, String elapsedTime, int likedBoardCnt, boolean isLikedBoard, double lat,
			double lng) {
		this.bno = bno;
		this.title = title;
		this.year = year;
		this.month = month;
		this.elapsedTime = elapsedTime;
		this.likedBoardCnt = likedBoardCnt;
		this.isLikedBoard = isLikedBoard;
		this.lat = lat;
		this.lng = lng;
	}
}
