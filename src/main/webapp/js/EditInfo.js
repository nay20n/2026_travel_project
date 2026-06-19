$(function() {
	/***************** 헤더 ************/
	// 트래플 플래너 로그 클릭
	$("#header > div:nth-child(1)").click(function() {
		location.href="MainPage.html";
	});
	/************** 기본정보 ************/
	// 프로필 연필 수정 버튼 
	$("#basicInfo > div:nth-child(2) > svg").click(function() {
		alert("프로필 사진 수정");
	});
	//이메일 변경 버튼  
	$("#basicInfo > div:nth-child(2) > div:nth-child(4) > button").click(function(){
		
		let email = $("#basicInfo > div:nth-child(2) > div:nth-child(4) > input").val();
		
		if(email == ""){
			$("#basicInfo > div:nth-child(2) > div:nth-child(4) > div:nth-child(3)").show();
			$("#basicInfo > div:nth-child(2) > div:nth-child(4) > input").addClass("borderWraning");
		} else {
			$("#basicInfo > div:nth-child(2) > div:nth-child(4) > div:nth-child(3)").hide();
			$("#basicInfo > div:nth-child(2) > div:nth-child(4) > input").removeClass("borderWraning");
			$(".popupContainer").show();
		}
	});
	// 팝업창 닫기
	$(".popupContent > svg:nth-child(1)").click(function() {
		$(".popupContainer").hide();
	});
	//취소 버튼 
	$("#basicInfo > div:nth-child(2) > div:nth-child(5) > button:nth-child(1)").click(function(){
		alert("취소되었습니다.");
	});
	//저장버튼
	$("#basicInfo > div:nth-child(2) > div:nth-child(5) > button:nth-child(2)").click(function(){
		alert("저장되었습니다.");
	});
	/***********비밀번호**************/
	//비밀번호 변경 버튼 클릭 
	$("#pw > div:nth-child(2) > button").click(function(){
		location.href="SetPw.html";
	});
	/***********계정연동*************/
	//연결해제 버튼 
	$("#connect > div:nth-child(2) > div > button").click(function() {
		
		if($(this).hasClass("connect")){ // 연결하기를 가지고 있는 클래스(즉, 연결해제 버튼)
			$(this).removeClass("connect");
			$(this).text("연결하기");
			$(this).parent().find("span").eq(1).remove();
		} else { //연결하기 버튼)
			$(this).addClass("connect");
			$(this).text("연결해제");
			$(this).after("<span>연결됨<span>");
		}
	});
});