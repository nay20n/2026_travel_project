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
	// **************캘린더********************
	// 일 단위로 이동
	$(".changeView > span:nth-child(1)").click(function() {
		location.href="MainDay.html";
	});
	// 주 단위로 이동
	$(".changeView > span:nth-child(2)").click(function() {
		location.href="MainWeek.html";
	});
	// 일정확정하기
	$("#main>button").click(function() {
		location.href="Board.html";
	});
});