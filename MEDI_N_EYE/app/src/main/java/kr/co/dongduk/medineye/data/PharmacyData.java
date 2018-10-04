package kr.co.dongduk.medineye.data;

import java.io.Serializable;

public class PharmacyData implements Serializable {

	//xml순서로 작성함
	String pharmacy_id;
	int cnt; //건수
	double distance; //거리
	String duty_addr; //주소
	String duty_div; //병원분류 약국->H
	String duty_name; //약국명
	String duty_tel; //대표전화
	int end_time; //진료 끝나는 시간
	double latitude; //위도 37.~
	double longitude; //경도 127.~
	int start_time;//진료 시작 시간

	public PharmacyData(){}

	public PharmacyData(String pharmacy_id, String duty_div, String duty_name,
						String duty_tel, String duty_addr, double longitude,
						double latitude, int cnt, double distance, int start_time,
						int end_time) {
		super();
		this.pharmacy_id = pharmacy_id;
		this.duty_div = duty_div;
		this.duty_name = duty_name;
		this.duty_tel = duty_tel;
		this.duty_addr = duty_addr;
		this.longitude = longitude;
		this.latitude = latitude;
		this.cnt = cnt;
		this.distance = distance;
		this.start_time = start_time;
		this.end_time = end_time;
	}
	
	public void clear()
	{
		this.pharmacy_id = "";
		this.duty_div =  "";
		this.duty_name =  "";
		this.duty_tel =  "";
		this.duty_addr =  "";
		this.longitude = 0.0;
		this.latitude = 0.0;
		this.cnt =  0;
		this.distance =  0;
		this.start_time =  0;
		this.end_time =  0;
	}

	public String getPharmacy_id() {
		return pharmacy_id;
	}

	public void setPharmacy_id(String pharmacy_id) {
		this.pharmacy_id = pharmacy_id;
	}

	public String getDuty_div() {
		return duty_div;
	}

	public void setDuty_div(String duty_div) {
		this.duty_div = duty_div;
	}

	public String getDuty_name() {
		return duty_name;
	}

	public void setDuty_name(String duty_name) {
		this.duty_name = duty_name;
	}

	public String getDuty_tel() {
		return duty_tel;
	}

	public void setDuty_tel(String duty_tel) {
		this.duty_tel = duty_tel;
	}

	public String getDuty_addr() {
		return duty_addr;
	}

	public void setDuty_addr(String duty_addr) {
		this.duty_addr = duty_addr;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getStart_time() {
		return start_time;
	}

	public void setStart_time(int start_time) {
		this.start_time = start_time;
	}

	public int getEnd_time() {
		return end_time;
	}

	public void setEnd_time(int end_time) {
		this.end_time = end_time;
	}

}
