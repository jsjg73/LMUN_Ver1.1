
 function check(){
	 if($.trim($("#email").val())==""){
		 alert("전자우편 입력");
		 $("#email").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd1").val())==""){
		 alert("비밀번호 입력");
		 $("#pwd").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd2").val())==""){
		 alert("비밀번호 다시 입력");
		 $("#join_pwd2").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd1").val()) != $.trim($("#pwd2").val())){
		 alert("비밀번호 불일치");
		 $("#pwd1").val("");
		 $("#pwd2").val("");
		 $("#pwd1").focus();
		 return false;
	 }
	 if($.trim($("#nickname").val())==""){
		 alert("별명 입력");
		 $("#nickname").val("").focus();
		 return false;
	 }
	 if($.trim($("#addr").val())==""){		 
		 alert("주소 입력");
		 $("#addr").val("").focus();
		 return false;
	 } 	 
 }
 
//email duplicate check
function email_check(){
	$("#emailcheck").hide();//span area
	var email=$("#email").val();
	//verification
	if(!(validate_useremail(email))){
		var newtext='<font color="red">전자우편 형식 오류</font>';
		$("#emailcheck").text('');	
		$("#emailcheck").show();	//span id area
		$("#emailcheck").append(newtext);
		$("#email").val("").focus();
		return false;
	};
	

	//email duplicate check
    $.ajax({
        type:"POST",
        url:"member_emailcheck.do",
        data: {"email":email},        
        success: function (data) { 
        	/*alert("return success="+data);*/
      	  if(data==1){	//id exists
      		var newtext='<font color="red">중복 전자우편</font>';
      			$("#emailcheck").text('');
        		$("#emailcheck").show();
        		$("#emailcheck").append(newtext);
          		$("#email").val('').focus();
          		return false;
	     
      	  }else{		//id not exists
      		var newtext='<font color="blue">사용 가능 전자우편</font>';
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
      });//$.ajax	
};


function validate_useremail(mememail)
{
  var pattern= new RegExp(/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i);
    
  //regular expression
  return pattern.test(mememail);
};


 //member information update alert
function edit_check(){
	if($.trim($("#pwd1").val())==""){
		 alert("비밀번호 입력");
		 $("#join_pwd1").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd2").val())==""){
		 alert("비밀번호 다시 입력");
		 $("#join_pwd2").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd1").val()) != $.trim($("#pwd2").val())){
		 alert("비밀번호 불일치");
		 $("#join_pwd1").val("");
		 $("#join_pwd2").val("");
		 $("#join_pwd1").focus();
		 return false;
	 }
	 if($.trim($("#nickname").val())==""){
		 alert("별명 입력");
		 $("#join_name").val("").focus();
		 return false;
	 }
	 if($.trim($("#addr").val())==""){
		 alert("주소 입력");
		 $("#addr").val("").focus();
		 return false;
	 }
} 
 