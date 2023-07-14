package com.ogive.oheo.dto;

import org.springframework.data.domain.Sort.Direction;

import com.ogive.oheo.constants.RoleTypes;
import com.ogive.oheo.constants.ServiceProviderType;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

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
	private Long id;
	
	private String lastName;
	
	private Long userId;
	
	private RoleTypes roleTypes;
	
	private String vehicleTypeName;
	
	private String fuelType;
	
	private RoleTypes role;
	
	private ServiceProviderType type;
	
	private String  vehicleBodyType;
	
	

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

	
	

}
