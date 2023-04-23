package com.ogive.oheo.dto;

public class ZipcodeDropDown {

	private Object code;

	private Object cityName;

	public ZipcodeDropDown(Object code, Object cityName) {
		super();
		this.code = code;
		this.cityName = cityName;
	}

	public Object getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public Object getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		return "ZipcodeDropDown [code=" + code + ", cityName=" + cityName + "]";
	}

}
