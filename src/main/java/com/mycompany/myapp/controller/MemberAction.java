package com.mycompany.myapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.myapp.model.FriendBean;
import com.mycompany.myapp.model.FriendPush;
import com.mycompany.myapp.model.MemberBean;
import com.mycompany.myapp.service.FriendService;
import com.mycompany.myapp.service.Memberservice;

@Controller
public class MemberAction {

	@Autowired
	private Memberservice memberService;
	@Autowired
	private FriendService friendService;

	//email duplicate check ajax
	@RequestMapping(value = "/member_emailcheck.do", method = RequestMethod.POST)
	public String member_emailcheck(@RequestParam("email") String email, Model model) throws Exception {

		int result = memberService.checkMemberEmail(email);
		model.addAttribute("result", result);

		return "member/emailcheckResult";
	}
	
	//sign in form
	@RequestMapping(value = "/member_login.do")
	public String member_login() {
		return "member/loginform";
	}

	//sign up form
	@RequestMapping(value = "/member_join.do")
	public String member_join() {
		return "member/joinform";
	}

	//member information save
	@RequestMapping(value = "/member_join_ok.do", method = RequestMethod.POST)
	public String member_join_ok(@ModelAttribute MemberBean member) throws Exception {
		memberService.insertMember(member);

		return "redirect:member_login.do";
	}

	
	//sign in verification
	@RequestMapping(value = "/member_login_ok.do", method = RequestMethod.POST)
	public String member_login_ok_push(HttpSession session,@RequestParam("email") String email,
			                      @RequestParam("pwd") String pwd,
			                       Model model) throws Exception {

		int result=0;		
		MemberBean m = memberService.userCheck(email);

		if (m == null) {	//member not exists
			result = 1;
			model.addAttribute("result", result);
			
			return "member/loginResult";
			
		} else {			//member exists
			if (m.getPwd().equals(pwd)) {	//correct password
				session.setAttribute("email", email);

	            String nickname = m.getNickname();
	            session.setAttribute("nickname", nickname);
	            /*
	             * db에서 친구 리스트 가져오기
	             */

	            List<FriendBean> list = friendService.list(email);
	            List<MemberBean> friendList = new ArrayList<MemberBean>();
	            for(FriendBean fb : list) {
	            	if(fb.getInviter().equals(email))
	            		friendList.add(memberService.userCheck(fb.getInvitee()));
	            	else
	            		friendList.add(memberService.userCheck(fb.getInviter()));
	            }
	            session.setAttribute("fr_list",friendList);
	            //받은 친구 요청 리스트(aop)
	            //List<FriendConfirm> invitedList = friendService.invited(email);
	            //session.setAttribute("fr_push",invitedList);
	            
	            
	            return "redirect:main.do";
			} else {		//incorrect password
				result = 2;
				model.addAttribute("result", result);
				
				return "member/loginResult";				
			}
		}
	}

	//member information update form
	@RequestMapping(value = "/member_edit.do")
	public String member_edit(HttpSession session, Model model) throws Exception {

		String email = (String) session.getAttribute("email");

		MemberBean m = memberService.userCheck(email);
				
		model.addAttribute("user", m);
		
		return "member/editform";
	}
	
	//member information update
	@RequestMapping(value = "/member_edit_ok.do", method = RequestMethod.POST)
	public String member_edit_ok( MemberBean member, HttpSession session, Model model) throws Exception {
		String email = member.getEmail();
				
		MemberBean editm = this.memberService.userCheck(email);
			
		if (!member.getPwd().equals(editm.getPwd())) {

			return "member/editResult";
			
		}else {	//correct password
			memberService.updateMember(member);
			session.invalidate();

			return "redirect:member_login.do";
		}
	}
		
	//sign out
	@RequestMapping("/member_logout.do")
	public String logout(HttpSession session) {
		session.invalidate();

		return "map/home";
	}
	
	//member deletion form
	@RequestMapping(value = "/member_del.do")
	public String member_del(HttpSession session, Model model) throws Exception {
		
		String email = (String) session.getAttribute("email");
		MemberBean user = memberService.userCheck(email);
		model.addAttribute("user", user);

		return "member/delform";
	}

	//member deletion completed
	@RequestMapping(value = "/member_del_ok.do", method = RequestMethod.POST)
	public String member_del_ok(MemberBean member, 
							    HttpSession session) throws Exception {
		//넘어온 이메일
		String email = member.getEmail();
		MemberBean user = this.memberService.userCheck(email);

		if (!member.getPwd().equals(user.getPwd())) {

			return "member/deleteResult";
			
		} else {	//correct password
			
			memberService.deleteMember(email);

			session.invalidate();

			return "redirect:member_login.do";
		}
	}

		//	member information
		@RequestMapping(value = "/member_info.do")
		public String member_info(HttpSession session, MemberBean member, Model model) throws Exception {

			String email = (String) session.getAttribute("email");
			
			if(email==null) { // 로그인 세션이 없을때
				model.addAttribute("result",3);
				return "member/loginResult"; //세션 없음 띄워주고 로그인 화면으로 이동
			}else {
				
				MemberBean m = memberService.userCheck(email);
				
				model.addAttribute("user", m);
			}
			
			
			return "member/userinfo";
		}
		
		//member information update form
		@RequestMapping(value = "/member_session.do")
		public String member_session(HttpSession session, Model model) throws Exception {
			
			return "member/sessionResult";
		}

}
