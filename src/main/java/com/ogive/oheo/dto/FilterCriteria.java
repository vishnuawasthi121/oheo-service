package com.ogive.oheo.dto;

import java.util.Arrays;

import org.springframework.data.domain.Sort.Direction;

import com.ogive.oheo.constants.StatusCode;

public class FilterCriteria {

	private int page;

	private int size;

	private String filterByName;

	private String filterByCode;

	private String filterByCityName;

	private String filterByStatus;

	private Direction sortDirection;

	private String[] orderBy;

	private StatusCode status;
	private String email;

	public FilterCriteria() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FilterCriteria(int page, int size, String filterByName, Direction sortDirection,String[] orderBy, StatusCode status) {
			
		super();
		this.page = page;
		this.size = size;
		this.filterByName = filterByName;
		this.sortDirection = sortDirection;
		this.orderBy = orderBy;
		this.status = status;
	}

	public FilterCriteria(int page, int size, String filterByCode, String filterByCityName, Direction sortDirection,StatusCode status) {
		super();
		this.page = page;
		this.size = size;
		this.filterByCode =filterByCode;
		this.filterByCityName = filterByCityName;
		this.sortDirection = sortDirection;
		this.status = status;
	}

	public String getFilterByCityName() {
		return filterByCityName;
	}

	public void setFilterByCityName(String filterByCityName) {
		this.filterByCityName = filterByCityName;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getFilterByName() {
		return filterByName;
	}

	public void setFilterByName(String filterByName) {
		this.filterByName = filterByName;
	}

	public String getFilterByStatus() {
		return filterByStatus;
	}

	public void setFilterByStatus(String filterByStatus) {
		this.filterByStatus = filterByStatus;
	}

	public Direction getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(Direction sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String[] getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String[] orderBy) {
		this.orderBy = orderBy;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public String getFilterByCode() {
		return filterByCode;
	}

	public void setFilterByCode(String filterByCode) {
		this.filterByCode = filterByCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	

}
