$(function() {
	// *************헤더****************
	// 메인로고 누르면 메인화면으로
	$("#header>div:nth-child(1)").click(function() {
		location.href="MainPage.html";
	});
	// 로그인 팝업
	$("#header> div:nth-child(2)>div:nth-child(1)>div").click(function() {
		$(this).parent().find(".hide").addClass("hide");
		$(".popupContainer").attr("style","display: block");
		$(".loginpop").attr("style","display: block");
	});
	// 로그아웃 알림
	$("#header> div:nth-child(2)>div:nth-child(2)>div").click(function() {
		alert("로그아웃 되었습니다!");
	});
	// 마이페이지 이동
	$("#header> div:nth-child(2)>div:nth-child(3)>div").click(function() {
		location.href="MyPage.html";
	});
	// ************ 메인 *******************
	// 직접 여행 만들기 (로그인 되었을 경우)
	$("#main>div:nth-child(5)").click(function() {
		location.href="ArrPlace.html";
	});
	// ************ 게시글 *******************
	// 검색(돋보기 클릭) 시 옆 글자 바꾸기
	$("#board>div>div>input").keypress(function(e) {
		if(e.keyCode == 13){
			$("#board>div:nth-child(1)>div>svg").trigger("click");
		}
	});
	$("#board>div:nth-child(1)>div>svg").click(function() {
		let search = $(this).parent().find("input").val();
		$("#board>div:nth-child(1)>span").html(search);
	});
	// 게시글로 이동
	$("#board>div>div>img:nth-child(1)").click(function() {
		location.href="Board.html";
	});
	// 하트 on off
	$("#board>div:NOT(:first-child)>div>div:nth-child(2)>span>svg").click(function() {
		$(this).toggleClass("fillHeart");
	});
	// ************** 팝업 ******************
	// 팝업창 닫기
	$(".popupContainer>div>svg:nth-child(1)").click(function() {
		$(".popupContainer").attr("style","display: none");
		$(".popupContent").attr("style","display: none");
	});
	// 비밀번호 재설정 페이지로 이동
	$(".popupContainer>div:nth-child(1)>div:nth-child(3)>div:nth-child(4)>span:nth-child(2)").click(function() {
		location.href="ResetPw.html";
	});
	// 로그인 알림
	$(".userPw, .userEmail").keypress(function(e) {
		if(e.keyCode == 13){
			$(".login").trigger("click");
		}
	});
	$(".login").click(function() {
		if($(this).parent().find(".userEmail").val()=="a"&&$(this).parent().find(".userPw").val()=="a") {
			$(this).parent().find(".hide").addClass("hide");
			alert("로그인 되었습니다!");
			$(".popupContainer").attr("style","display: none");
			$(".popupContent").attr("style","display: none");
		}else {
			$(this).parent().find(".hide").removeClass("hide");
			alert("불일치!");
		}
	});
	// 이메일로 가입하기
	$(".popupContainer>div:nth-child(1)>div:nth-child(4)>div:nth-child(3)").click(function() {
		$(".popupContainer>div:nth-child(1)").hide();
		$(".popupContainer>div:nth-child(2)").show();
	});
	// 이메일로 가입하기-인증번호 받기
	$("body>div.popupContainer>div:nth-child(2)>div>div.inputBdDiv>input").keypress(function(e) {
		if(e.keyCode == 13){
			$(".popupContainer > div:nth-child(2) > div:nth-child(2) > div:nth-child(4)").trigger("click");
		}
	});
	$(".popupContainer > div:nth-child(2) > div:nth-child(2) > div:nth-child(4)").click(function() {
		if($(this).parent().find(".checkBox").is(":checked")) {
			$(".popupContainer>div:nth-child(2)").hide();
			$(".popupContainer>div:nth-child(3)").show();
		}else {
			alert("약관에 동의하지 않으면 가입할 수 없습니다.");
			return;		
		}
	});
	// 이메일로 가입하기-뒤로가기
	$(".popupContainer > div:nth-child(2) > div:nth-child(2) > div:nth-child(5)").click(function() {
		$(".popupContainer>div:nth-child(2)").hide();
		$(".popupContainer>div:nth-child(1)").show();
	});
	// 이메일로 가입하기-인증번호 받기-인증완료
	$(".popupContainer > div:nth-child(3) > div:nth-child(2) > div:nth-child(5)").click(function() {
		if($(this).parent().find(".checkBox").is(":checked")) {
			location.href="SetPw.html";
		}else {
			alert("약관에 동의하지 않으면 가입할 수 없습니다.");
			return;		
		}
	});
});