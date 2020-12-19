package com.mycompany.myapp.service;

import java.util.List;
import java.util.Map;

import com.mycompany.myapp.model.FriendBean;
import com.mycompany.myapp.model.FriendPush;

public interface FriendService {

	int push_confirm(Map m) throws Exception;

	int checkFriend(Map m) throws Exception;

	List<FriendBean> list(String email);

	int delFriend(Map<String, String> m) throws Exception;

	int checkMemberEmail(String email) throws Exception;

	List<FriendPush> invite(String email);

	List<FriendPush> invited(String email);

	int accept(FriendPush fc);

	int checkFriendConfirm(Map m) throws Exception;

	List<FriendBean> recommend(String email);

	int del(FriendPush fc);

	int accept(Map<String, String> m);

}