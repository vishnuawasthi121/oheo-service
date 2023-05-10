package com.ogive.oheo.constants;

public enum RoleTypes {

	Admin("admin"), Distributor("distributor "), Dealer("dealer"), Subdealer("subdealer"), Customer("customer");

	private String value;

	public String getValue() {
		return value;
	}

	private RoleTypes(String value) {
		this.value = value;
	}
}
