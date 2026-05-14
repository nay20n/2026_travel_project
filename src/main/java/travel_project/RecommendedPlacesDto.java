package travel_project;

public class RecommendedPlacesDto {
	 String rcPlaceId; //추천장소id
	 String name; //장소이름
	 double lat; //위도
	 double lng; //경도
	 RecommendedPlacesDto(String rcPlaceId, String name, double lat, double lng) {
		 this.rcPlaceId = rcPlaceId; this.name = name;
		 this.lat = lat; this.lng = lng;
	 }
}
