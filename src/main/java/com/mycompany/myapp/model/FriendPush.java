package com.mycompany.myapp.model;

public class FriendPush {
	private String inviter; 
	private String invitee; 
	public String getInviter() {
		return inviter;
	}
	public void setInviter(String inviter) {
		this.inviter = inviter;
	}
	public String getInvitee() {
		return invitee;
	}
	public void setInvitee(String invitee) {
		this.invitee = invitee;
	}
	@Override
	public String toString() {
		return "FriendConfirm [inviter=" + inviter + ", invitee=" + invitee + "]";
	}
	
}
