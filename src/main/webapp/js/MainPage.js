$(function() {
	// 메인로고 누르면 메인화면으로
	$(".mainlogo").click(function() {
		location.href="MainPage.html";
	});
	// 로그인 팝업
	$(".login").click(function() {
		$(".popup-container").attr("style","display: block");
		$(".loginpop").attr("style","display: block");
	});
	// 팝업창 닫기
	$(".popup-container> div > svg:nth-child(1)").click(function() {
		$(".popup-container").attr("style","display: none");
		$(".popup-content").attr("style","display: none");
	});
});