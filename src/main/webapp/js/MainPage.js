$(function() {
	// 메인로고 누르면 메인화면으로
	$(".mainLogo").click(function() {
		location.href="MainPage.html";
	});
	// 로그인 팝업
	$(".login").click(function() {
		$(".popupContainer").attr("style","display: block");
		$(".loginpop").attr("style","display: block");
	});
	// 팝업창 닫기
	$(".popupContainer> div > svg:nth-child(1)").click(function() {
		$(".popupContainer").attr("style","display: none");
		$(".popupContent").attr("style","display: none");
	});
	// 비밀번호 재설정 페이지로 이동
	$(".popupContainer>div:nth-child(1)>div:nth-child(3)>div:nth-child(4)>span:nth-child(2)").click(function() {
		location.href="ResetPw.html";
	});
});