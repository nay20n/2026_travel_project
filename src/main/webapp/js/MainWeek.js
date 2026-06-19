$(function() {
	// **************캘린더********************
	// 일 단위로 이동
	$(".changeView > span:nth-child(1)").click(function() {
		location.href="MainDay.html";
	});
	// 월 단위로 이동
	$(".changeView > span:nth-child(3)").click(function() {
		location.href="MainMonth.html";
	});
	// 일정확정하기
	$("#main > div:nth-child(2)>button:nth-child(3)").click(function() {
		location.href="Board.html";
	});	
});