package travel_project;

public class CommentDto {
	String profileImg; //프로필사진
	int memberId; //멤버ID
	String writerNick; //닉네임
	String content; //내용
	String date; //작성일
	
	CommentDto (String profileImg, int memberId, String writerNick, String content, String date) {
		this.profileImg = profileImg;
		this.memberId = memberId;
		this.writerNick = writerNick;
		this.content = content;
		this.date = date;
	}
}
