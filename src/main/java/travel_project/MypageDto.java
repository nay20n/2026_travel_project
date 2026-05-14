package travel_project;

public class MypageDto {
	int bno; //게시글 번호
	String dDay; //디데이 
	String title; //일정 이름
	String writerNick; //작성자 
	boolean isLiked; //찜 여부 
	int likeBoardCnt; //찜 개수
	
	MypageDto(int bno, String dDay, String title, String writerNick, boolean isLiked, int likeBoardCnt){
		this.bno = bno;
		this.dDay = dDay;
		this.title = title;
		this.writerNick = writerNick;
		this.isLiked = isLiked;
		this.likeBoardCnt = likeBoardCnt;
	}
}
