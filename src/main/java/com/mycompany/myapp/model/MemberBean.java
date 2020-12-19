package com.mycompany.myapp.model;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

@Repository
public class MemberBean implements Serializable{

	private String email;
	private String nickname;
	private String pwd;
	private String addr1;
	private String addr2;
	private String addr3;
	private String x_; //경도
	private String y_; //위도
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getAddr3() {
		return addr3;
	}
	public void setAddr3(String addr3) {
		this.addr3 = addr3;
	}
	
	public String getX_() {
		return x_;
	}
	public void setX_(String x_) {
		this.x_ = x_;
	}
	public String getY_() {
		return y_;
	}
	public void setY_(String y_) {
		this.y_ = y_;
	}
	@Override
	public String toString() {
		return "MemberBean [email=" + email + ", nickname=" + nickname + ", pwd=" + pwd + ", addr1=" + addr1
				+ ", addr2=" + addr2 + ", addr3=" + addr3 + ", x_=" + x_ + ", y_=" + y_ + "]";
	}
}
