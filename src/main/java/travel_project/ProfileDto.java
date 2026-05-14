package travel_project;

public class ProfileDto {
	 String profile; //프로필 사진
	 String nickName; //닉네임
	 int myTavelCnt; //작성한 일정 수
	 int myLikeCnt; //찜한 수
	 int myCommentCnt; //댓글 쓴 수
	 ProfileDto(String profile, String nickName, int myTavelCnt, int myLikeCnt, int myCommentCnt) {
		 this.profile = profile; this.nickName = nickName;
		 this.myTavelCnt = myTavelCnt; this.myLikeCnt = myLikeCnt;
		 this.myCommentCnt = myCommentCnt;
	 }
}
