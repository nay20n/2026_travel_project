// 별점(1), 퍼센트(50%) 들어오면 그래프 그려주는 함수
function setGraph(rate, per) {
	$(".graph").each(function(idx, item) {
		if(5-idx==Number(rate)) {
			let width = "width: " + per;
			$(this).attr("style",width);
		}
	});
}
// 평균별점 들어오면 별점 그려주는 함수
function setAvgStar(rate) {
	let width = "width: " + (Number(rate)/5*100) + "%";
	$(".placeDetail>div:nth-child(5)>div:nth-child(2)>div:nth-child(2)>div").attr("style",width);
}
// 색 배열 들어오면 블럭 초기 색상 넣어주는 함수 + 임시 색 배열
let blockColorArr = ["#fff4e6","#b0e0e6","#9fb5c8","#f7d9c4","#e2cfd4"];
function setInitialColor(blockColorArr) {
	$(".blockColor").each(function(idx, item) {
		color = "color: " + blockColorArr[idx];
		$(this).attr("style",color);
	});
}

$(function() {
	setInitialColor(blockColorArr);
	// *************헤더****************
	// 메인로고 누르면 메인화면으로
	$("#header>div:nth-child(1)>div:nth-child(1)").click(function() {
		location.href="MainPage.html";
	});
	// 공유
	$("#header> div:nth-child(2)>div:nth-child(1)>div").click(function() {
		alert("링크가 복사되었습니다!");	
	});
	// 마이페이지 이동
	$("#header> div:nth-child(2)>div:nth-child(2)>div").click(function() {
		location.href="MyPage.html";
	});
	// ************장소검색****************
	// 검색 엔터로 돋보기 클릭
	$("#main>div.bs>div.inputBdDiv>label>input").keypress(function(e) {
		if(e.keyCode == 13){
			$("#main > div:nth-child(1) > div:nth-child(1) > label > svg").trigger("click");
		}
	});
	// 이름 클릭 시 정보창 팝업
	$(".placeTitle>div:nth-child(1)>a").click(function(){
		$(".popupContainer").removeClass("hide");
		$(".popupContainer>div:nth-child(1)").removeClass("hide");
	});
	// 장소 옆 별 on off
	$(".placeTitle>div>svg").click(function(){
		$(this).toggleClass("fillStar");
	});
	// ************** 팝업 ******************
	// 팝업창 닫기
	$(".popupContent>svg:nth-child(1)").click(function() {
		$(".popupContainer").addClass("hide");
		$(".popupContent").addClass("hide");
	});
	// ************** 장소 정보창 *************
	// 장소 옆 별 on off
	$(".popupPlace>div:nth-child(1)>div:nth-child(2)>svg").click(function(){
		$(this).toggleClass("fillStar");
	});
	// 영업시간 on off
	$(".placeDetail > div:nth-child(2) > svg:NOT(:first-child)").click(function(){
		$(".placeDetail > div:nth-child(2) > svg:NOT(:first-child)").toggleClass("hide");
		$(".placeDetail > div:nth-child(3)").toggleClass("hide");
	});
	// 평점 별 on off
	$(".reviewInput>div:nth-child(1)>svg").click(function(){
		if($(this).hasClass("fillStar")) {
			$(".reviewInput>div:nth-child(1)>svg").removeClass("fillStar");
		}else {
			let rate = $(this).index();
			$(".inputStar").each(function(idx, item) {
				if(idx>rate) return;
				$(this).addClass("fillStar");
			});
		}
	});
	// 댓글 사진 추가
	$(".reviewInput>div:nth-child(3)>div:nth-child(1)").click(function() {
		alert("사진 삽입!");
	});
	// 댓글 등록
	$(".reviewInput>div:nth-child(3)>div:nth-child(2)").click(function() {
		alert("등록!!");
	});
	// 댓글 수정
	$(".placeReview>div:nth-child(1)>div:nth-child(2)>div:nth-child(1)").click(function() {
		alert("수정!");
	});
	// 댓글 삭제
	$(".placeReview>div:nth-child(1)>div:nth-child(2)>div:nth-child(2)").click(function() {
		alert("삭제!");
	});
	// *************** 블럭 정보 팝업 *****************
	// (임시) 캘린더 있는 곳 클릭 시 팝업
	$(".calendar").click(function() {
		$(".popupContainer").removeClass("hide");
		$(".popupContainer>div:nth-child(2)").removeClass("hide");
	});
	// 블럭 색 바꾸기 창 띄우기
	$(".setBlockColor").click(function() {
		$(".popupContainer>div:nth-child(3)").toggleClass("hide");
	});
	// 블럭 색 지정하기
	$(".blockColor").click(function() {
		let colorIdx = $(this).index();
		let color = "color: " + blockColorArr[colorIdx];
		$(".setBlockColor").attr("style",color);
		$(".popupContainer>div:nth-child(3)").addClass("hide");
	});
	// *************** 교통 팝업 *****************
	// (임시) 캘린더 클릭 시 팝업
	$(".calendar").click(function() {
		$(".popupContainer").removeClass("hide");
		$(".popupContainer>div:nth-child(4)").removeClass("hide");
	});
	// 교통 클릭 시 닫힘
	$(".popupContainer>div:nth-child(4)>span").click(function() {
		$(".popupContainer").addClass("hide");
		$(".popupContainer>div:nth-child(4)").addClass("hide");
	});
});