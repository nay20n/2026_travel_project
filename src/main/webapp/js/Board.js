// 예산 금액 합 갱신
function getSum() {
	let sum = 0;
	$(".fee").each(function(idx,item) {
		let fee = Number($(this).html().replace("원","").replace("\,",""));
		sum += fee;
	});
	$("#content1>div:nth-child(2)>div:nth-child(2)>div:nth-child(7)>span:nth-child(2)").html(sum+"원");
}
$(function() {
	// *************헤더****************
	// 메인로고 누르면 메인화면으로
	$("#header>div:nth-child(1)>div:nth-child(1)").click(function() {
		location.href="MainPage.html";
	});
	// 로그아웃 알림 및 메인화면으로
	$("#header> div:nth-child(2)>div:nth-child(1)>div").click(function() {
		alert("로그아웃 되었습니다!");
		location.href="MainPage.html";		
	});
	// 마이페이지 이동
	$("#header> div:nth-child(2)>div:nth-child(2)>div").click(function() {
		location.href="MyPage.html";
	});
	// *************예산****************
	// 게시글 복제
	$(".other>div").click(function() {
		alert("게시글이 복제되었습니다!");
		location.href="StartPlace.html";
	});
	// 삭제
	$(".my>div:nth-child(1)").click(function() {
		alert("정말 삭제하시겠습니까?");
		location.href="MainPage.html";
	});
	// 수정
	$(".my>div:nth-child(2)").click(function() {
		location.href="MainWeek.html";
	});
	// AI 비용
	$(".my>div:nth-child(3)").click(function() {
		alert("계산해드립니당");
	});
	// 캘린더 동기화
	$(".my>div:nth-child(4)").click(function() {
		alert("동기화!");
	});
	// *******************댓글*********************
	// 찜 색칠
	$("#content3 > div:nth-child(1) > div > div:nth-child(1)").click(function() {
		$(this).find("svg").toggleClass("fillHeart");;
	});
	// 댓글 수정
	$(".comment>div>div:nth-child(2)>div:nth-child(1)").click(function() {
		alert("수정 버튼!");
	});
	// 댓글 삭제
	$(".comment>div>div:nth-child(2)>div:nth-child(2)").click(function() {
		alert("정말 삭제 하시나요?");
	});
	// 댓글 등록
	$("#commentInput").parent().find("div").click(function(){
		alert("등록~");
	});
});