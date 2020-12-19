package com.mycompany.myapp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.model.FriendBean;
import com.mycompany.myapp.model.FriendPush;
import com.mycompany.myapp.model.MemberBean;

@Repository
public class FriendDAOImpl {

	@Autowired
	private SqlSession sqlSession;
	
	public int accept(Map<String, String> m) {
		return sqlSession.insert("friendns.insert_fr", m);
	}
	
	// member information save
//	@Transactional
	public int push_confirm(Map m) throws Exception {
//		confirm table test
		return sqlSession.insert("friendns.add_fr_push", m);
	}

	public int checkFriend(Map m) throws Exception {

		int re = 1;

		FriendBean fb = (FriendBean) sqlSession.selectOne("friendns.check_friend", m);

		if (fb != null)
			re = -1;
		return re;
		// return sqlSession.selectOne("friendns.check_friend", m);
	}

	public List<FriendBean> list(String email) {
		return sqlSession.selectList("friendns.list", email);
	}

	public int delFriend(Map<String, String> m) throws Exception {
		return sqlSession.delete("friendns.del_friend", m);
	}
	public List<FriendPush> invite(String email){
		return sqlSession.selectList("friendns.invite", email);
	}

	public List<FriendPush> invited(String email) {
		return sqlSession.selectList("friendns.invited", email);
	}

	public int accept(FriendPush fc) {
		
		return sqlSession.update("friendns.accept",fc);
	}
	
	public int checkFriendConfirm(Map m) throws Exception {

		FriendPush fc = (FriendPush) sqlSession.selectOne("friendns.check_friendpush", m);
		if (fc != null)
			if (fc.getInviter().equals(m.get("inviter"))){ //요청 보낸 로그
				return -2;
			}else { // 요청 받은 로그
				return -3;
			}
		
		return 1;
		// return sqlSession.selectOne("friendns.check_friend", m);
	}

	public List<FriendBean> recommend(String email) {
		return sqlSession.selectList("recommend",email);
	}

	public int del(FriendPush fc) {
		return sqlSession.delete("friendns.pushDel", fc);
	}

	
	
}