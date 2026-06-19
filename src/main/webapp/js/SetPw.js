$(function(){
	/***************** 헤더 ************/
	// 트래플 플래너 로그 클릭
	$("#header > div").click(function() {
		location.href="MainPage.html";
	});
	
	/*******저장버튼**********/
	$("#main > div:last-child > button").click(function() {
		let pw = $("#pwInput > div:nth-child(1) > input").val();
		let pwConfirm = $("#pwInput > div:nth-child(2) > input").val() 
		let isAllValid = true; 
		
		//둘다 채워져 있을 때 
		if(pw!="" && pwConfirm!=""){
			
			if(pw=="a"){// 영문자 어쩌구가 아닐 경우 
				$("#pwInput > div:nth-child(1) > div:nth-child(3)").text("비밀번호는 영문자, 숫자, 특수문자 모두 포함한 8~20자여야 합니다.");
				$("#pwInput > div:nth-child(1) > div:nth-child(3)").addClass("pwWarning");
				$("#pwInput > div:nth-child(1) > input").addClass("borderWraning");
				isAllValid = false;
			} else { // 잘 입력했을 경우 
				$("#pwInput > div:nth-child(1) > div:nth-child(3)").text("비밀번호 (영문자, 숫자 특수문자 포함 최소 8~20자)");
				$("#pwInput > div:nth-child(1) > div:nth-child(3)").removeClass("pwWarning");
				$("#pwInput > div:nth-child(1) > input").removeClass("borderWraning");
			}
			
			if(pw != pwConfirm){ //두개가 다른 내용일 때 
				$("#pwInput > div:nth-child(2) > div:nth-child(3)").addClass("show");
				$("#pwInput > div:nth-child(2) > input").addClass("borderWraning");
				isAllValid = false;
			} else { //두개가 같을 때
				$("#pwInput > div:nth-child(2) > div:nth-child(3)").removeClass("show");
				$("#pwInput > div:nth-child(2) > input").removeClass("borderWraning");
			}
			
			if(isAllValid) alert("비밀번호가 변경되었습니다.");
			
		} else { // 둘중 하나라도 비워져 있을 때 
			alert("정보를 입력하세요!");
		}
	});
});