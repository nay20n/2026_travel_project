$(function() {
	/***************** 헤더 ************/
	// 트래플 플래너 로그 클릭
	$("#header > div:nth-child(1)").click(function() {
		location.href="MainPage.html";
	});
	//로그아웃 버튼 클릭 
	$("#header > div:nth-child(2) > div:nth-child(1)> a").click(function() {
		alert("로그아웃 되었습니다.");
		location.href="MainPage.html";
	});
	//마이페이지 버튼 클릭
	$("#header > div:nth-child(2) > div:nth-child(2) > a").click(function() {
		location.href="MyPage.html";
	});
	
	/***************** 테이블 ******************/
	// 테이블 행 클릭시 게시글로 이동 
	$(".table th:not(:last-child)").click(function() {
		//location.href="MyBoard.html";
		location.href="Board.html";
	});
	$(".table svg").click(function() {
		$(this).toggleClass("fillHeart");
	});
	
	/***************** 정보수정 팝업창 ************/
	// 정보수정 버튼 클릭
	$("#profile > div:nth-child(6)").click(function() { 
		$("#popupEditInfo").show();
	});
	// 정보창 닫기 버튼 클릭
	$("#popupHeader > svg").click(function() {
		$("#popupEditInfo").hide();
	});
	// 정보수정팝업 확인 버튼 클릭(정보수정창으로 이동)
	$("#popupPw > div > span").click(function() {
	
		if($("#popupPw > div > input").val()==""){ //비밀번호가 비어잇으면
			$("#popupContent > div:nth-child(3)").show();
			$("#popupPw > div > input").addClass("borderWraning");
		}else {
			$("#popupContent > div:nth-child(3)").hide();
			location.href="EditInfo.html";
		}
	});
	// 비밀번호 재설정 링크 클릭 
	$("#popupContent > div:nth-child(4) > a").click(function() {
		location.href="ResetPw.html";
	});
	// 구글 계정 인증 버튼 클릭
	$("#popupVerify > img").click(function() {
		alert("구글 인증하는 API 연결하기");
	});
	
});