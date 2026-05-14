package travel_project;

public class BlockDto {
	int blockIdx; //블록 인덱스
	String startTime; // 시작 시간
	String endTime; //끝 시간
	boolean isCheckedAi; //ai필수 반영 여부, 이동시간
	int colorIdx; //색번호
	String colorCode; //색상코드
	String name; //장소이름
	String category; //장소분류
	String address; //주소
	double lat; //위도
	double lng; //경도
	
	BlockDto(int blockIdx, String startTime, String endTime, boolean isCheckedAi, int colorIdx, String colorCode, String name, String category, String address, double lat, double lng) {
		this.blockIdx = blockIdx;
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
