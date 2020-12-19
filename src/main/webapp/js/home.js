// 출발지 검색에 사용됨(joinform.jsp)========================================
var keyword = "";
var places = new kakao.maps.services.Places();
// 지도 중심위치 설정
var latlng = new kakao.maps.LatLng(37.5668260054857, 126.978656785931);
var options = {
	location : latlng,
	size : 5
};
var doubleSubmitFlag = false;
function doubleSubmitCheck(){
    if(doubleSubmitFlag){
        return doubleSubmitFlag;
    }else{
        doubleSubmitFlag = true;
        return false;
    }
}
$(document).ready(
		function() {
			$('#fr_request').mouseover(function(){
				$('#search').hide();
			});
			$('#fr_request').mouseout(function(){
				$('#search').show();
			});
			$('.w3-dropdown-content').mouseover(function(){
				$('#search').hide();
			});
			$('.w3-dropdown-content').mouseout(function(){
				$('#search').show();
			});
			$('#check_data').click(
					function() {
						if(doubleSubmitCheck())
							return false;
						
						if ($('.place_values').length < 2) {
							alert("주소지 정보를 2개 이상 입력해주세요");
							doubleSubmitFlag=false;
							return false;
						}
						//$('#check_data').attr("disabled");
						$('#check_data').html('<span class="spinner-border spinner-border-sm"></span>');
						// controller에서 객체 리스트로 매핑하기위한 코드.
						// submit 버튼 눌렀을 때 작동하는 이벤트
						// name을 places[0].x, places[0].y .. 처럼 설정해주면
						// controller에서 dto 리스트로 받을 수 있음
						// 참고
						// http://noveloper.github.io/blog/spring/2015/02/16/how-mapping-to-model-arrayvalue.html						
						$("#form .place_values").each(
								function(index) {
									$(this).find("input[name=x]").attr("name","places[" + index + "].x");
									$(this).find("input[name=y]").attr("name",
											"places[" + index + "].y");
									$(this).find("input[name=name]").attr(
											"name",
											"places[" + index + "].name");
									$(this).find("input[name=address]").attr(
											"name",
											"places[" + index + "].address");
								})

					});

			$('#search').click(function() {

				check = /[a-zA-Z0-9가-힣]/; // 검색 형식 검사(숫자, 영어 대소문자, 한글(자음따로
				// 모음따로는 검색 x))
				keyword = $("#search_bar").val().trim();
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

			$('#friend-list').click(function() {

			});
		});

var search_type = "keyword";
// api 에서 데이터 받아오고 modal에 보여주기.
var callback = function(result, status) {
	if (status === kakao.maps.services.Status.OK) {
		// console.log(search_type);
		$("#table_part tbody").empty();
		// console.log(result);
		for ( var i in result) {

			var p_name = "";
			if (search_type == 'keyword') {
				p_name = result[i].place_name;
			} else { // search_type='geo'
				p_name = result[i].address_name;
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
				var str = "<tr><td class='btn'>" + p_name + "<br>"
						+ "<p class=w3-opacity style='font-size: 12px'>"
						+ result[i].address_name + "</p>"
						+ "<input name='hh' type='hidden' " + "data-name='"
						+ p_name + "' data-x='" + result[i].x + "' data-y='"
						+ result[i].y + "' data-addr='"
						+ result[i].address_name + "'></td></tr>";
				// ===================================================================

				$("#table_part tbody").append(str);
				// ------20/08/04 권은지 : 유효성 검사 추가3 / 수도권 지역(서울, 경기, 인천) 아닌 지역
				// 검색결과에서 제외 -------
			} else {
				continue;
			}
			// --------------------------------------------------------------------------------------
		}
	} else if (search_type == 'keyword') {
		search_type = "geo";
		var geocoder = new kakao.maps.services.Geocoder();
		return geocoder.addressSearch(keyword, callback);
	} else {
		$("#table_part tbody").empty();
		$("#table_part tbody").append("검색 결과가 없습니다.");
	}
	search_type = "keyword";
};
// 20/08/10 권은지 추가 : modal에서 주소 선택했을 때 클릭
// 이벤트=======================================================================
// 동적으로 생성되는 modal-body 테이블에서 값을 읽어오기 위해 바뀐 함수
$(document).on('click','td.btn',function() {
					console.log(this);
					var name = $(this).find("input[name=hh]").data('name');
					var x = $(this).find("input[name=hh]").data('x');
					var y = $(this).find("input[name=hh]").data('y');
					var addr_name = $(this).find("input[name=hh]").data('addr');

					if ($('li.w3-bar').length == 10) {
						alert("최대10개");
					} else {
						var tag = '<li class="w3-bar"><span onclick="remove(this)" '
								+ ' data-x="'
								+ x
								+ '" class="w3-bar-item w3-button w3-white w3-xlarge w3-right">×</span>'
								+ '<div class="w3-bar-item"><span class="w3-large">'
								+ name
								+ '</span><br> <span class="w3-addr">'
								+ addr_name + '</span></div></li>';

						var str = "<div class='place_values'>"
								+ "<input type='hidden' name='name' value='"
								+ name
								+ "'/><input type='hidden' name='x' value='"
								+ x
								+ "'/><input type='hidden' name='address' value='"
								+ addr_name
								+ "'/><input type='hidden' name='y' value='"
								+ y + "'></div>";

						$('.w3-ul.w3-card-4').append(tag); // 메인 화면에서 사용자에게 선택한
															// 장소 보여주기.

						$("#form").append(str); // 데이터 넘기기위해 input type hidden으로
												// append

						$('button.close').click();
						$("#table_part tbody").empty();
					}
				});
// ==========================================================================

// 선택된 출발지에서 삭제 버튼 누를 때 실행
function remove(it) {
	var check = confirm('삭제하시겠습니까?');
	if(check){
		var x = $(it).data("x");
		var parent = $(it).closest("li").remove();
		var target = $('input[value="' + x + '"]').closest('div').remove();

		// 세션 낱개 삭제
		// serialize 함수를 사용하면 form 태그 내 input 태그에 입력된 값을 전부 연결하여 전달한다.
		var frmData = $('#form').serialize(); // 아이디값

		$.post('session_del.do', frmData, function(data) {
			alert("삭제됐습니다.");
		});
	}
};
function fr_down(btn) {
	if ($('li.w3-bar').length == 10) {
		alert("최대10개");
	} else {
		alert("추가됐습니다.")
		var x_ = $(btn).data("x_");
		var y_ = $(btn).data("y_");
		var nick = $(btn).data("nickname");
		var addr = $(btn).data("addr");

		var tag = '<li class="w3-bar"><span onclick="remove(this)" '
				+ ' data-x="'
				+ x_
				+ '" class="w3-bar-item w3-button w3-white w3-xlarge w3-right">×</span>'
				+ '<div class="w3-bar-item"><span class="w3-large">' + nick
				+ '</span><br> <span class="w3-addr">' + addr
				+ '</span></div></li>';

		var str = "<div class='place_values'>"
				+ "<input type='hidden' name='name' value='" + nick
				+ "'/><input type='hidden' name='x' value='" + x_
				+ "'/><input type='hidden' name='address' value='" + addr
				+ "'/><input type='hidden' name='y' value='" + y_ + "'></div>";

		$('.w3-ul.w3-card-4').append(tag); // 메인 화면에서 사용자에게 선택한 장소 보여주기.

		$("#form").append(str); // 데이터 넘기기위해 input type hidden으로 append
	}

};

function frpush(inviter, status){	
	var check = true;
	if(status=="1"){
		check=confirm("수락하시겠습니까?");
	}else{
		check=confirm("거절하시겠습니까?");
	}
	if(check){
		$.ajax({
			type:"POST",
	        url:"friend_accept.do",
	        data: {"inviter":inviter,
	        	"status":status
	        },
	        dataType:'text', 
	        success: function (data){
				//페이지 특정 부분만 리로드
	        	$("#fr-accept").load(window.location.href + " #fr-accept");
				//location.reload(true);
	        }
		});
	}
	
};
