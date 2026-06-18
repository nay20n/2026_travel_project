$(function (){
	/***************** 헤더 ************/
	// 트래플 플래너 로그 클릭
	$("#header > div").click(function() {
		location.href="MainPage.html";
	});
	
	/***************** 메인박스 ************/
	$("#main > button").click(function() {
		
		let email = $("#main > input").val();
		
		if(email=="") alert("이메일을 입력하세요.");
		else alert("비밀번호 재설정 메일을 보냈습니다.");
	});
});