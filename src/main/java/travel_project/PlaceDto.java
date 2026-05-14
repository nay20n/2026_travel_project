package travel_project;

public class PlaceDto {
	String placeId; //장소 고유 id
	String name; //장소이름
	String category; //카테고리
	String address; //주소
	double lat; //위도
	double lng; //경도
	String img; //장소 사진 
	double avgRating; //평균 별점 
	int reviewCnt; //리뷰수 
	boolean isLikedPlace; //장소 찜 유무

	PlaceDto(String placeId, String name, String category, String address, double lat, double lng, String img, double avgRating, int reviewCnt, boolean isLikedPlace) {
		this.placeId = placeId;
		this.name = name;
		this.category = category;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.img = img;
		this.avgRating = avgRating;
		this.reviewCnt = reviewCnt;
		this.isLikedPlace = isLikedPlace;
	}

}

