package com.ogive.oheo.dto.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.persistence.entities.ChargingPartDealer;
import com.ogive.oheo.persistence.entities.ChargingStation;

public class ChargingMngtSpec {

	// ChargingPartDealer - Filters
	public static Specification<ChargingPartDealer> filterChargingPartDealerByStatus(FilterCriteria filter) {
		return new Specification<ChargingPartDealer>() {
			@Override
			public Predicate toPredicate(Root<ChargingPartDealer> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}
	
	public static Specification<ChargingPartDealer> filterChargingPartDealerByName(FilterCriteria filter) {
		return new Specification<ChargingPartDealer>() {
			@Override
			public Predicate toPredicate(Root<ChargingPartDealer> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}
	
	//ChargingStation
	
	public static Specification<ChargingStation> filterChargingStationByStatus(FilterCriteria filter) {
		return new Specification<ChargingStation>() {
			@Override
			public Predicate toPredicate(Root<ChargingStation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}

	public static Specification<ChargingStation> filterChargingStationByName(FilterCriteria filter) {
		return new Specification<ChargingStation>() {
			@Override
			public Predicate toPredicate(Root<ChargingStation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}
}
