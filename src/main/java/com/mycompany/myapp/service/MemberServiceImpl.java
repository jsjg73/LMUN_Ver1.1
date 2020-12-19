package com.mycompany.myapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.dao.MemberDAOImpl;
import com.mycompany.myapp.model.MemberBean;

@Service
public class MemberServiceImpl implements Memberservice {
	
	@Autowired
	private MemberDAOImpl memberDao;
	
	@Override
	public int checkMemberEmail(String email) throws Exception{
		return memberDao.checkMemberEmail(email);
	}
	
//	public MemberBean findpwd(MemberBean m)throws Exception {
//		return memberDao.findpwd(m);
//	}
	
	@Override
	public void insertMember(MemberBean member) throws Exception{
		
		memberDao.insertMember(member);
	}
	
	@Override
	public MemberBean userCheck(String id) throws Exception{
		return memberDao.userCheck(id);
	}
	
	@Override
	public void updateMember(MemberBean member) throws Exception{
		memberDao.updateMember(member);
	}
	
	@Override
	public void deleteMember(String email) throws Exception{
		memberDao.deleteMember(email);
	}
}
