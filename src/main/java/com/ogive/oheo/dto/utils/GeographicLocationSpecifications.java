package com.ogive.oheo.dto.utils;

import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.Company;
import com.ogive.oheo.persistence.entities.PurchaseType;
import com.ogive.oheo.persistence.entities.UserDetail;
import com.ogive.oheo.persistence.entities.VehicleBodyType;
import com.ogive.oheo.persistence.entities.VehicleDetail;
import com.ogive.oheo.persistence.entities.VehicleFuelType;
import com.ogive.oheo.persistence.entities.VehicleModel;
import com.ogive.oheo.persistence.entities.VehicleTransmission;
import com.ogive.oheo.persistence.entities.VehicleType;
import com.ogive.oheo.persistence.entities.ViewUserDetails;
import com.ogive.oheo.persistence.entities.Zipcode;
import com.ogive.oheo.persistence.entities.ZoneDetail;

public class GeographicLocationSpecifications {

	public static Specification<ZoneDetail> filterByName(FilterCriteria filter) {

		return new Specification<ZoneDetail>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ZoneDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filter.getFilterByName().toUpperCase() + "%");
				//return criteriaBuilder.like(zone.get("name"), "%" + filter.getFilterByName() + "%");

				// builder.like(root.get("address"), "%" + address + "%");
			}
		};
	}

	public static Specification<Company> filterCompanyByCompanyName(FilterCriteria filter) {
		return new Specification<Company>() {
			@Override
			public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				//return criteriaBuilder.like(root.get("companyName"), "%" + filter.getFilterByName() + "%");
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("companyName")), "%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}
	public static Specification<Company> filterCompanyByStatus(FilterCriteria filter) {
		return new Specification<Company>() {
			@Override
			public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"),filter.getStatus());
			}
		};
	}
	
	
	public static Specification<VehicleModel> filterVehicleModelByModelName(FilterCriteria filter) {
		return new Specification<VehicleModel>() {
			@Override
			public Predicate toPredicate(Root<VehicleModel> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				//return criteriaBuilder.like(root.get("modelName"), "%" + filter.getFilterByName() + "%");
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("modelName")), "%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}
	
	public static Specification<VehicleModel> filterVehicleModelByStatus(FilterCriteria filter) {
	
		return (root, query, builder) -> {
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}
	 
	// Vehicle Type 
	public static Specification<VehicleType> filterVehicleTypeByName(FilterCriteria filter) {
		return new Specification<VehicleType>() {
			@Override
			public Predicate toPredicate(Root<VehicleType> root, CriteriaQuery<?> query,CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				//return criteriaBuilder.like(root.get("name"), "%" + filter.getFilterByName() + "%");
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}
	
	public static Specification<VehicleType> filterVehicleTypeByStatus(FilterCriteria filter) {
		return (root, query, builder) -> {
			
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}
	
	
	// Vehicle Fuel Type 
	public static Specification<VehicleFuelType> filterVehicleFuelTypeByName(FilterCriteria filter) {
		return new Specification<VehicleFuelType>() {
			@Override
			public Predicate toPredicate(Root<VehicleFuelType> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<VehicleFuelType> filterVehicleFuelTypeByStatus(FilterCriteria filter) {
		return (root, query, builder) -> {
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}
	
	//vehicle-transmissions
	public static Specification<VehicleTransmission> filterVehicleTransmissionByName(FilterCriteria filter) {
		return new Specification<VehicleTransmission>() {
			@Override
			public Predicate toPredicate(Root<VehicleTransmission> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<VehicleTransmission> filterVehicleTransmissionByStatus(FilterCriteria filter) {
		return (root, query, builder) -> {
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}
	// Vehicle Details 
	public static Specification<VehicleDetail> filterVehicleDetailByName(FilterCriteria filter) {
		return new Specification<VehicleDetail>() {
			@Override
			public Predicate toPredicate(Root<VehicleDetail> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<VehicleDetail> filterVehicleDetailByStatus(FilterCriteria filter) {
		return (root, query, builder) -> {
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}
	// Purchange Type 
	public static Specification<PurchaseType> filterPurchaseTypeByName(FilterCriteria filter) {
		return new Specification<PurchaseType>() {
			@Override
			public Predicate toPredicate(Root<PurchaseType> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<PurchaseType> filterPurchaseTypeByStatus(FilterCriteria filter) {
		return (root, query, builder) -> {
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}
	
	// Vehicle Body Types
	public static Specification<VehicleBodyType> filterVehicleBodyTypeByName(FilterCriteria filter) {
		return new Specification<VehicleBodyType>() {
			@Override
			public Predicate toPredicate(Root<VehicleBodyType> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<VehicleBodyType> filterVehicleBodyTypeByStatus(FilterCriteria filter) {
		return (root, query, builder) -> {
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}
	
	
	// Zone 
	public static Specification<ZoneDetail> filterByStatus(FilterCriteria filter) {
		return (root, query, builder) -> {
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}

	public static Specification<Zipcode> findZipcodeByCode(FilterCriteria filter) {
		return (root, query, criteriaBuilder) -> {
			if (ObjectUtils.isEmpty(filter.getFilterByCode())) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get("code"), filter.getFilterByCode());
		};
	}

	public static Specification<Zipcode> findZipcodeByCityName(FilterCriteria filter) {
		return (root, query, criteriaBuilder) -> {
			if (ObjectUtils.isEmpty(filter.getFilterByCityName())) {
				return criteriaBuilder.conjunction();
			}
			Join<Zipcode, City> zipcodeCity = root.join("city");
		//	return builder.like(zipcodeCity.get("name"), "%" + filter.getFilterByCityName() + "%");
			return criteriaBuilder.like(criteriaBuilder.upper(zipcodeCity.get("name")), "%" + filter.getFilterByCityName().toUpperCase() + "%");
		};

	}
	
	//User filter by name and status
	public static Specification<ViewUserDetails> filterUserDetailByName(FilterCriteria filter) {
		return new Specification<ViewUserDetails>() {
			@Override
			public Predicate toPredicate(Root<ViewUserDetails> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<ViewUserDetails> filterUserDetailByStatus(FilterCriteria filter) {
		return (root, query, builder) -> {
			if (ObjectUtils.isEmpty(filter.getStatus())) {
				return builder.conjunction();
			}
			return builder.equal(root.get("status"), filter.getStatus());
		};
	}

	public static Specification<ViewUserDetails> findAllUserDetailByRootId(FilterCriteria filter) {
		return (root, query, criteriaBuilder) -> {
			if (Objects.isNull(filter.getId())) {
				return criteriaBuilder.conjunction();
			}
			
		//	return builder.like(zipcodeCity.get("name"), "%" + filter.getFilterByCityName() + "%");
			return criteriaBuilder.equal(root.get("rootId"), filter.getId() );
		};

	}
}