package travel_project;

public class PlaceDto {
	String placeId; //장소 고유 id
	String name; //장소이름
	String category; //카테고리
	String address; //주소
	String bussinessHour; //요일별영업시간
	double lat; //위도
	double lng; //경도
	String webUrl; //공식사이트
	String img; //장소 사진 
	boolean isLikedPlace; //장소 찜 유무
}