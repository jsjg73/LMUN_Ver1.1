package com.mycompany.myapp.model;

import java.io.Serializable;
import java.util.List;

public class Place implements Serializable{
	private String name;
	private String x;
	private String y;
	private String address;
	private String phone;
	private String place_url;
	
	//route.jsp에서 각 도착지에 대해 id 부여
	private String id;
	
	//출발지 주소 받아올때 controller에서 
	//리스트로 매핑받기 위해 필요함
	private List<Place> places;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public List<Place> getPlaces() {
		return places;
	}
	public void setPlaces(List<Place> places) {
		this.places = places;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	@Override
	public String toString() {
		return "Place [name=" + name + ", x=" + x + ", y=" + y + ", address=" + address + "]";
	}
}
