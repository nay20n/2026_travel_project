package travel_project;

public class AiBlocksDto {
	int aiBlockIdx; // 블럭 인덱스
	int bno; //게시글 번호
	String placeId; //장소 id
	String aiStartTime; //시작 시간
	String aiEndTime; //끝 시간
	int aiTravelTime; //이동시간(분)
	
	AiBlocksDto(int aiBlockIdx, int bno, String placeId, String aiStartTime, String aiEndTime, int aiTravelTime) {
		this.aiBlockIdx = aiBlockIdx;
		this.bno = bno;
		this.placeId = placeId;
		this.aiStartTime = aiStartTime;
		this.aiEndTime = aiEndTime;
		this.aiTravelTime = aiTravelTime;
	}
}
