// 별점(1), 퍼센트(50%) 들어오면 그래프 그려주는 함수
function setGraph(rate, per) {
	$(".graph").each(function(idx, item) {
		if(5-idx==Number(rate)) {
			let width = "width: " + per;
			$(this).attr("style",width);
		}
	});
}

$(function() {
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
	// 이름 클릭 시 정보창 팝업
	$(".placeTitle>div:nth-child(1)>a").click(function(){
		$(".popupContainer").toggleClass("hide");
		$(".popupContainer>div:nth-child(1)").toggleClass("hide");
	});
	// 장소 옆 별 on off
	$(".placeTitle>div>svg").click(function(){
		$(this).toggleClass("fillStar");
	});
	// ************** 팝업 ******************
	// 팝업창 닫기
	$(".popupContent>svg:nth-child(1)").click(function() {
		$(".popupContainer").toggleClass("hide");
		$(this).parent().toggleClass("hide");
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
});