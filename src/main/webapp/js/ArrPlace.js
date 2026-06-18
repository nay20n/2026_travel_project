$(function(){
	// <-이미지 클릭하면 전 페이지로
	$("#header > img").click(function() {
		history.back();
	});
	
	$("#recomendPlace > div:nth-child(2) > span").click(function() {
	
		if($(this).hasClass("coloredPlace")){ // 선택한 span이라면 
			$(this).removeClass("coloredPlace");
			$("#nextBtn > div").removeClass("coloredBtn");
			return;
		}
	
		$(this).parent().find("span").removeClass("coloredPlace");
		$(this).addClass("coloredPlace");
		$("#nextBtn > div").addClass("coloredBtn");
		
		
	});
	
	
	// 다음 페이지 클릭하면 도착지 페이지로 이동
	$("#nextBtn > div").click(function(){
		location.href="Date.html";
	});
	
});