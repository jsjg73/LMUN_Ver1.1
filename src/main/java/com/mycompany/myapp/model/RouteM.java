package com.mycompany.myapp.model;

public class RouteM {
	private String id;
	private int num;
	private String name;
	private String x;
	private String y;
	private String address;
	private String phone;
	private String place_url;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPlace_url() {
		return place_url;
	}
	public void setPlace_url(String place_url) {
		this.place_url = place_url;
	}
	@Override
	public String toString() {
		return "RouteM [id=" + id + ", num=" + num + ", name=" + name + ", x=" + x + ", y=" + y + ", address="
				+ address + ", phone=" + phone + ", place_url=" + place_url + "]";
	}
	
}
