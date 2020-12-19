// 출발지 검색에 사용됨(joinform.jsp)========================================
	var keyword="";
	var places = new kakao.maps.services.Places();	
	// 지도 중심위치 설정
	var latlng = new kakao.maps.LatLng(37.5668260054857, 126.978656785931);
	var options = {
			location : latlng,
			size : 5
	};
$(document).ready(function(){	
	$('#search').click(function() {
		//var check = /\b(?:one|two|three)\b/gi;
		var check = /[a-zA-Z0-9가-힣]/; // 검색 형식 검사(숫자, 영어 대소문자, 한글(자음따로
		// 모음따로는 검색 x))
		keyword = $("#addr1").val().trim();
		if (keyword == "") {

			alert("검색어를 입력해주세요.");
			return false;
		}
		if (!check.test(keyword)) { // 검색어 유효성 검사
			alert("검색어가 유효하지 않습니다.");
			return false;
		}
			// ---------------------20/08/04 권은지 : 유효성 검사 추가2 / SQL
			// Injection 방지 필터링 처리 추가
		function SQLFiltering(str) {
				str = str.replace(/\s{1,}1\=(.*)+/, ""); // 공백이후 1=1이 있을
				// 경우 제거
				str = str.replace(/\s{1,}(or|and|null|where|limit)/i, " "); // 공백이후
				// or,
				// and
				// 등이
				// 있을
				// 경우
				// 제거
				str = str.replace(/[\s\t\'\;\=]+/, "");
				// 공백이나 탭 제거, 특수문자 제거
				return str;
			}
			// SQL Injection 방지 > 검색할 keyword 올바르게 replace하는 코드 (sql 예약어
			// 키워드로 API에 요청시 서버 멈춤)
			keyword = SQLFiltering(keyword);

			// keyword replace 디버깅 코드
			// console.log(keyword);
			// --------------------------------------------------------------

			// option으로 검색량 조절하기.
			places.keywordSearch(keyword, callback, options);
	});
});
var search_type ="keyword";
// api 에서 데이터 받아오고 modal에 보여주기.
var callback = function(result, status) {
	if (status === kakao.maps.services.Status.OK) {
		console.log(search_type);
		$("#table_part tbody").empty();
		console.log(result);
		for ( var i in result) {
			
			var p_name = "";
			if(search_type=='keyword'){
				p_name=result[i].place_name;
			}else{	// search_type='geo'
				p_name=result[i].address_name;
			}
			// ------------------20/08/04 권은지 : 유효성 검사 추가3 / 수도권 지역(서울, 경기, 인천)
			// 아닌 지역 검색결과에서 제외 -------
			/** * 서브스트링 함수 * string.cut(length)로 사용 * */
			String.prototype.cut = function(len) {
				var str = this;
				var l = 0;
				for (var i = 0; i < str.length; i++) {
					l += (str.charCodeAt(i) > 128) ? 2 : 1;
					if (l > len)
						return str.substring(0, i);
				}
				return str;
			};

			// p_region : 지역명 검사용 변수(앞 두 글자)
			var p_region = result[i].address_name.cut(4);
			// 디버깅 코드
			// console.log("p_region : ", p_region);
			if (p_region === '서울' || p_region === '인천' || p_region === '경기') {
				// 출처: https://darusamu.tistory.com/23 [Workspace]
				// --------------------------------------------------------------------------------------

				// 20/08/10 권은지 변경 : modal-body append 태그
				// 변경=========================
				var str = "<tr><td class='btn' data-dismiss='modal'>" + p_name + "<br>"
				+ "<p class=w3-opacity style='font-size: 12px'>"
				+ result[i].address_name + "</p>"
				+ "<input name='hh' type='hidden' "
				+ "data-name='" + p_name + "' data-x='" + result[i].x
				+ "' data-y='" + result[i].y + "' data-addr='"
				+ result[i].address_name
				+ "'></td></tr>";
				$("#table_part tbody").append(str);
				// ------20/08/04 권은지 : 유효성 검사 추가3 / 수도권 지역(서울, 경기, 인천) 아닌 지역
				// 검색결과에서 제외 -------
			} else {
				continue;
			}
		}
			// --------------------------------------------------------------------------------------		
		
	}else if(search_type=='keyword'){
		search_type="geo";
		var geocoder = new kakao.maps.services.Geocoder();
		return geocoder.addressSearch(keyword, callback);
	}else{
		$("#table_part tbody").empty();
		$("#table_part tbody").append("검색 결과가 없습니다.");
	}
	search_type ="keyword";
};
// ============================================================================


// modal에서 주소 선택했을 때 클릭 이벤트(joinform.jsp)======================================
/*function down(btn) {
	var name = $(btn).data("name");
	var x = $(btn).data("x");
	var y = $(btn).data("y");
	var addr_name = $(btn).data("addr");
	
	var tag ='<li class="w3-bar"><span onclick="this.parentElement.style.display=\'none\'"'+
						'class="w3-bar-item w3-button w3-white w3-xlarge w3-right">×</span>'+
						'<div class="w3-bar-item"><span class="w3-large">'+name+
						'</span><br> <span class="w3-addr">'+addr_name+
						'</span></div></li>';
	$('.w3-ul.w3-card-4').html(tag);
	
	$('input[name="addr1"]').attr('value',addr_name);
	$('input[name="x_"]').attr('value',x);
	$('input[name="y_"]').attr('value',y);
	$('button.close').click();
}*/
// ==========================================================================

// 20/08/11 권은지 주소 선택 변경
// =========================================================================
// 20/08/10 권은지 추가 : modal에서 주소 선택했을 때 클릭
// 이벤트=======================================================================
// 동적으로 생성되는 modal-body 테이블에서 값을 읽어오기 위해 바뀐 함수
$(document).on('click','td.btn', function(){
	console.log(this);
	var name = $(this).find("input[name=hh]").data('name');
	var x = $(this).find("input[name=hh]").data('x');
	var y = $(this).find("input[name=hh]").data('y');
	var addr_name = $(this).find("input[name=hh]").data('addr');

		
	var tag ='<li class="w3-bar"><span onclick="this.parentElement.style.display=\'none\'"'+
	'class="w3-bar-item w3-button w3-white w3-xlarge w3-right">×</span>'+
	'<div class="w3-bar-item"><span class="w3-large">'+name+
	'</span><br> <span class="w3-addr">'+addr_name+
	'</span></div></li>';
	$('.w3-ul.w3-card-4').html(tag);

	$('input[name="addr1"]').attr('value',addr_name);
	$('input[name="x_"]').attr('value',x);
	$('input[name="y_"]').attr('value',y);
	
});
// ==============================================================================
// =========================================================================



 function check(){
	 if($.trim($("#email").val())==""){
		 alert("전자우편 입력");
		 $("#email").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd").val())==""){
		 alert("비밀번호 입력");
		 $("#pwd").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd2").val())==""){
		 alert("비밀번호 다시 입력");
		 $("#join_pwd2").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd").val()) != $.trim($("#pwd2").val())){
		 alert("비밀번호 불일치");
		 $("#pwd").val("");
		 $("#pwd2").val("");
		 $("#pwd").focus();
		 return false;
	 }
	 if($.trim($("#nickname").val())==""){
		 alert("별명 입력");
		 $("#nickname").val("").focus();
		 return false;
	 }
	 if($.trim($("#addr1").val())==""){		 
		 alert("주소 입력");
		 $("#addr1").val("").focus();
		 return false;
	 } 	 
 }
 
// email duplicate check
function email_check(){
	$("#emailcheck").hide();// span area
	var email=$("#email").val();
	// verification
	if(!(validate_useremail(email))){
		var newtext='<font color="red">이메일 형식 오류</font>';
		$("#emailcheck").text('');	
		$("#emailcheck").show();	// span id area
		$("#emailcheck").append(newtext);
		$("#email").val("").focus();
		return false;
	};
	

	// email duplicate check
    $.ajax({
        type:"POST",
        url:"member_emailcheck.do",
        data: {"email":email},        
        success: function (data) { 
        	/* alert("return success="+data); */
      	  if(data==1){	// id exists
      		var newtext='<font color="red">중복된 이메일입니다.</font>';
      			$("#emailcheck").text('');
        		$("#emailcheck").show();
        		$("#emailcheck").append(newtext);
          		$("#email").val('').focus();
          		return false;
	     
      	  }else{		// id not exists
      		var newtext='<font color="blue">사용 가능 이메일입니다.</font>';
      		$("#emailcheck").text('');
      		$("#emailcheck").show();
      		$("#emailcheck").append(newtext);
      		$("#email").focus();
      	  }  	    	  
        }
        ,
    	  error:function(e){
    		  alert("data error"+e);
    	  }
      });// $.ajax
};


function validate_useremail(mail)
{
  var pattern= new RegExp(/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i);
    
  // regular expression
  return pattern.test(mail);
};


// editform.jsp에서 사용================================================
function edit_check(){
	if($.trim($("#pwd").val())==""){
		 alert("비밀번호 입력");
		 $("#join_pwd1").val("").focus();
		 return false;
	 }
	 if($.trim($("#nickname").val())==""){
		 alert("별명 입력");
		 $("#join_name").val("").focus();
		 return false;
	 }
	 if($.trim($("#addr1").val())==""){
		 alert("주소 입력");
		 $("#addr").val("").focus();
		 return false;
	 }
}

/*
 * // delform.jsp ==========================================================
 * function del_check(){ if($.trim($("#pwd").val())==""){ alert("비밀번호 입력");
 * $("#pwd").val("").focus(); return false; } }
 */
 