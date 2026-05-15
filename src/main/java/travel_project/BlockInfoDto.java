package travel_project;

public class BlockInfoDto {
	String startTime; // 시작시간
	String endTime; // 끝시간
	boolean isCheckedAi; // ai반영여부
	int colorIdx; // 색 인덱스
	String colorCode; // 색 코드
	String name; // 장소이름
	String category; // 장소 카테고리
	String address; // 장소 주소
	double lat; // 위도
	double lng; // 경도

	BlockInfoDto(String startTime, String endTime, boolean isCheckedAi, int colorIdx, String colorCode, String name,
			String category, String address, double lat, double lng) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.isCheckedAi = isCheckedAi;
		this.colorIdx = colorIdx;
		this.colorCode = colorCode;
		this.name = name;
		this.category = category;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}
}
