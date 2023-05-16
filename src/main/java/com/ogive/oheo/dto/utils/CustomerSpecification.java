package com.ogive.oheo.dto.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.persistence.entities.CustomerDetail;

public class CustomerSpecification {

	public static Specification<CustomerDetail> filterCustomerByFirstName(FilterCriteria filter) {
		return new Specification<CustomerDetail>() {
			@Override
			public Predicate toPredicate(Root<CustomerDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("firstName")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
				// return criteriaBuilder.like(root.get("name"), "%" + filter.getFilterByName()
				// + "%");
			}
		};
	}

	public static Specification<CustomerDetail> filterCustomerByLastName(FilterCriteria filter) {
		return new Specification<CustomerDetail>() {
			@Override
			public Predicate toPredicate(Root<CustomerDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getLastName())) {
					return criteriaBuilder.conjunction();
				}

				return criteriaBuilder.like(criteriaBuilder.upper(root.get("lastName")),
						"%" + filter.getLastName().toUpperCase() + "%");
				// return criteriaBuilder.like(root.get("name"), "%" + filter.getFilterByName()
				// + "%");
			}
		};
	}
}
