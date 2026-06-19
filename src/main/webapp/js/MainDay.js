$(function() {
	// *************캘린더*****************
	// 주 클릭.
	$(".changeView > span:nth-child(2)").click(function() {
		location.href="MainWeek.html";
	});
	// 월 클릭.
	$(".changeView > span:nth-child(3)").click(function() {
		location.href="MainMonth.html";
	});
	// ************** AI 추천 일정 ***************
	// 열기
	$("#main>div:nth-child(2)>button:nth-child(4)").click(function() {
		$("#aiCalendar").removeClass("hide");
	});
	// 닫기
	$("#dailyMap > div:nth-child(2) > div:nth-child(1) > svg:nth-child(2)").click(function() {
		$("#aiCalendar").addClass("hide");
	});
	// 반영하고 닫기
	$("#dailyMap > div:nth-child(2) > div:nth-child(1) > svg:nth-child(3)").click(function() {
		alert("정말 반영하시겠습니까? 기존 하루 일정이 삭제됩니다.");
		$("#aiCalendar").addClass("hide");
	});
});