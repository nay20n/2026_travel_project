package travel_project;

public class ReviewDto {
	 String content; //댓글 내용
	 int rating; //별점
	 String date; //작성일
	 String reviewImg; //리뷰이미지
	 String writerNick; //작성자닉
	 String writerProfile; //작성자프로필이미지
	 ReviewDto(String content, int rating, String date, String reviewImg, String writerNick, String writerProfile) {
		 this.content = content; this.rating = rating; this.date = date;
		 this.reviewImg = reviewImg; this.writerNick = writerNick;
		 this.writerProfile = writerProfile;
	 }
}
