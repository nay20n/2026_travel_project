package travel_project;

public class BoardInfoDto {
	 int writerId; //게시글 작성자
	 String title; //게시글 제목
	 String startPlaceId; //출발지id
	 String arrPlaceId; //여행지id
	 String startDate; //여행시작일
	 String endDate; //여행종료일
	 int maxCost; //AI예상최대비용
	 int transportCost; //교통비
	 int foodCost; //식비
	 int roomCost; //숙소비
	 int ectCost; //기타 경비
	 int commentCnt; //게시글의 댓글 수
	 boolean isLikeBorad; //찜 여부
	 int likeBoardCnt; //찜 개수
	 int blockIdx; //블럭인덱스
	 String startTime; //블럭 시작시간
	 String endTime; //블럭 끝시간
	 int colorIdx; //블럭 색깔 인덱스
	 String colerCode; //블럭색깔코드
	 String name; //장소이름
}
