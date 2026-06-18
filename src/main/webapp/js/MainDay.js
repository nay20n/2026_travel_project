$(function() {


	// 일 클릭.
	$(".changeView > span:nth-child(1)").click(function() {
		location.href="MainDay.html";
	});
	// 주 클릭.
	$(".changeView > span:nth-child(2)").click(function() {
		location.href="MainWeek.html";
	});
	// 월 클릭.
	$(".changeView > span:nth-child(3)").click(function() {
		location.href="MainMonth.html";
	});
});