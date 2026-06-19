$(function (){
	/***************** 헤더 ************/
	// 트래플 플래너 로그 클릭
	$("#header > div").click(function() {
		location.href="MainPage.html";
	});
	
	/***************** 메인박스 ************/
	$("#main > button").click(function() {
		
		let email = $("#main > input").val();
		
		if(email=="a"){  //이메일을 잘 입력했을 떄
			$("#main > div:nth-child(7)").removeClass("show");
			$("#main > input").removeClass("borderWraning");
			alert("비밀번호 재설정 메일을 보냈습니다.");
		}
		else{ //이메일을 잘못 압력 못햇을 떄
			$("#main > div:nth-child(7)").addClass("show");
			$("#main > input").addClass("borderWraning");
		}
	});
});