package com.ogive.oheo.constants;

public enum ImageType {

	Slider("slider"), Brochure("brochure"),Product("product"),Other("other");

	private String value;

	public String getValue() {
		return value;
	}

	ImageType(String value) {
		this.value = value;
	}

}
