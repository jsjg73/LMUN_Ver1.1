package com.mycompany.myapp.service;

import com.mycompany.myapp.model.MemberBean;

public interface Memberservice {

	int checkMemberEmail(String email) throws Exception;

	void insertMember(MemberBean member) throws Exception;

	MemberBean userCheck(String id) throws Exception;

	void updateMember(MemberBean member) throws Exception;

	void deleteMember(String email) throws Exception;

}