package travel_project;

public class PlaceInfoDto {
	String name; // 장소 이름
	double avgRating; // 평균별점
	int reviewCnt; // 리뷰수
	String category; // 카테고리
	String address; // 주소
	String businessHours; // 요일별영업시간
	String webUrl; // 공식사이트 
	String img; // 이미지
	boolean isLikedPlace; // 장소찜유무
	
	PlaceInfoDto(String name, double avgRating, int reviewCnt, String category, String address, String businessHours, String webUrl, String img, boolean isLikedPlace) {
		this.name = name; this.avgRating = avgRating; this.reviewCnt = reviewCnt;
		this.category = category; this.address = address; this.businessHours = businessHours;
		this.webUrl = webUrl; this.img = img; this.isLikedPlace = isLikedPlace;
	}
}
