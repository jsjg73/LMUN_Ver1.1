package com.mycompany.myapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.dao.FriendDAOImpl;
import com.mycompany.myapp.dao.MemberDAOImpl;
import com.mycompany.myapp.model.FriendBean;
import com.mycompany.myapp.model.FriendPush;
import com.mycompany.myapp.model.MemberBean;


@Service
public class FriendServiceImpl implements FriendService {
	
	@Autowired
	private MemberDAOImpl memberDao;
	@Autowired
	private FriendDAOImpl friendDao;
	
	
	@Override
	public int accept(Map<String, String> m) {
		return friendDao.accept(m);
	}
	
	@Override
	public int push_confirm(Map m) throws Exception{
		return friendDao.push_confirm(m);
	}
	
	@Override
	public int checkFriend(Map m) throws Exception{
		return friendDao.checkFriend(m);
	}
	
	@Override
	public List<FriendBean> list(String email){
		return friendDao.list(email);
	}

	@Override
	public int delFriend(Map<String, String> m) throws Exception{
		return friendDao.delFriend(m);
	}
	
	@Override
	public int checkMemberEmail(String email) throws Exception{
		return memberDao.checkMemberEmail(email);
	}
	
	@Override
	public List<FriendPush> invite(String email){
		return friendDao.invite(email);
	}

	@Override
	public List<FriendPush> invited(String email) {
		return friendDao.invited(email);
	}

	@Override
	public int accept(FriendPush fc) {
		return friendDao.accept(fc);
	}
	@Override
	public int checkFriendConfirm(Map m) throws Exception {
		return friendDao.checkFriendConfirm(m);
	}

	@Override
	public List<FriendBean> recommend(String email) {
		return friendDao.recommend(email);
	}
	@Override
	public int del(FriendPush fc) {
		return friendDao.del(fc);
	}
}
