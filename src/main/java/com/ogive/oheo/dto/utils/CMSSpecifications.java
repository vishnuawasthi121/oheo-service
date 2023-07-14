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
import com.ogive.oheo.persistence.entities.BuyRequest;
import com.ogive.oheo.persistence.entities.ChargingPartDealer;
import com.ogive.oheo.persistence.entities.ChargingProduct;
import com.ogive.oheo.persistence.entities.ChargingStationBuyRequest;
import com.ogive.oheo.persistence.entities.Images;
import com.ogive.oheo.persistence.entities.LogisticRequest;
import com.ogive.oheo.persistence.entities.Product;
import com.ogive.oheo.persistence.entities.ServiceProviders;
import com.ogive.oheo.persistence.entities.UserDetail;
import com.ogive.oheo.persistence.entities.VehicleBodyType;
import com.ogive.oheo.persistence.entities.VehicleDetail;
import com.ogive.oheo.persistence.entities.VehicleFuelType;
import com.ogive.oheo.persistence.entities.VehicleMaintenanceRecord;
import com.ogive.oheo.persistence.entities.VehicleType;

public class CMSSpecifications {

	
	public static Specification<Product> filterLiveProduct() {

		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("isLive")), "Y");
			}
		};
	}

	// CMS V2 API
	public static Specification<Product> filterProductByName(FilterCriteria filter) {

		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<Product> filterProductByStatus(FilterCriteria filter) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}
	
	// FuelType filter 
	public static Specification<Product> filterProductByFuelType(FilterCriteria filter) {
		
		return new Specification<Product>() {
		
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			
				if (ObjectUtils.isEmpty(filter.getFuelType())) {
					return criteriaBuilder.conjunction();
				}
				Join<Product, VehicleFuelType> vehicleFuelType = root.join("vehicleFuelType");
				return criteriaBuilder.like(criteriaBuilder.upper(vehicleFuelType.get("name")),"%" + filter.getFuelType().toUpperCase() + "%");
			}
		};
	}
	
	public static Specification<Product> filterProductByVehicleType(FilterCriteria filter) {

		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getVehicleTypeName())) {
					return criteriaBuilder.conjunction();
				}
				// VehicleType vehicleType
				Join<Product, VehicleType> vehicleType = root.join("vehicleType");
				return criteriaBuilder.like(criteriaBuilder.upper(vehicleType.get("name")),
						"%" + filter.getVehicleTypeName().toUpperCase() + "%");
			}
		};
	}
	
	// Filter product based on the Vehicle Body Type.
	//Product->VehicleDetail->VehicleBodyType
	public static Specification<Product> filterProductByVehicleBodyType(FilterCriteria filter) {

		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				if (ObjectUtils.isEmpty(filter.getVehicleBodyType())) {
					return criteriaBuilder.conjunction();
				}
				// VehicleDetail vehicleDetail
				Join<Product, VehicleDetail> vehicleDetail = root.join("vehicleDetail");
				if(Objects.isNull(vehicleDetail)) {
					return criteriaBuilder.conjunction();
				}
				//VehicleBodyType vehicleBodyType
				Join<VehicleDetail, VehicleBodyType> vehicleBodyType = vehicleDetail.join("vehicleBodyType");
				if(Objects.isNull(vehicleBodyType)) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(vehicleBodyType.get("name")),
						"%" + filter.getVehicleBodyType().toUpperCase() + "%");
			}
		};
	}
	
	
	public static Specification<Product> onlyFetchLoggedInUserProduct(FilterCriteria filter) {
		
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getUserId())) {
					return criteriaBuilder.conjunction();
				}
				Join<Product, UserDetail> userDetail = root.join("userDetail");
				return criteriaBuilder.equal(userDetail.get("id"), filter.getUserId());
			}
		};
	}

		public static Specification<Images> filterImagesByName(FilterCriteria filter) {
		return new Specification<Images>() {
			@Override
			public Predicate toPredicate(Root<Images> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
				// return criteriaBuilder.like(root.get("name"), "%" + filter.getFilterByName()
				// + "%");
			}
		};
	}

	public static Specification<Images> filterImagesByStatus(FilterCriteria filter) {
		return new Specification<Images>() {
			@Override
			public Predicate toPredicate(Root<Images> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}

	
	//BuyRequest 
	public static Specification<BuyRequest> filterBuyRequestByName(FilterCriteria filter) {
		return new Specification<BuyRequest>() {
			@Override
			public Predicate toPredicate(Root<BuyRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
				// return criteriaBuilder.like(root.get("name"), "%" + filter.getFilterByName()
				// + "%");
			}
		};
	}

	public static Specification<BuyRequest> filterBuyRequestByEmail(FilterCriteria filter) {
		return new Specification<BuyRequest>() {
			@Override
			public Predicate toPredicate(Root<BuyRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getEmail())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("email")),
						"%" + filter.getEmail().toUpperCase() + "%");
			}
		};
	}
	
	
	public static Specification<BuyRequest> filterBuyRequestByUserId(FilterCriteria filter) {
		return new Specification<BuyRequest>() {
			@Override
			public Predicate toPredicate(Root<BuyRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getUserId())) {
					return criteriaBuilder.conjunction();
				}
				
				Join<BuyRequest, UserDetail> userDetail = root.join("userDetail");
				return criteriaBuilder.equal(userDetail.get("id"), filter.getUserId());
			}
		};
	}
	
	//ChargingStationBuyRequest
	public static Specification<ChargingStationBuyRequest> filterChargingStationBuyRequestByEmail(FilterCriteria filter) {
		return new Specification<ChargingStationBuyRequest>() {
			@Override
			public Predicate toPredicate(Root<ChargingStationBuyRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getEmail())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("email")),
						"%" + filter.getEmail().toUpperCase() + "%");
			}
		};
	}
	
	public static Specification<ChargingStationBuyRequest> filterChargingStationBuyRequestByName(FilterCriteria filter) {
		return new Specification<ChargingStationBuyRequest>() {
			@Override
			public Predicate toPredicate(Root<ChargingStationBuyRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}
	
	// Charging Products 
	public static Specification<ChargingProduct> filterChargingProductByName(FilterCriteria filter) {
		return new Specification<ChargingProduct>() {
			@Override
			public Predicate toPredicate(Root<ChargingProduct> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
				// return criteriaBuilder.like(root.get("name"), "%" + filter.getFilterByName()
				// + "%");
			}
		};
	}

	public static Specification<ChargingProduct> filterChargingProductByStatus(FilterCriteria filter) {
		return new Specification<ChargingProduct>() {
			@Override
			public Predicate toPredicate(Root<ChargingProduct> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}
	
	public static Specification<ChargingProduct> onlyFetchLoggedInUserChargingProduct(FilterCriteria filter) {

		return new Specification<ChargingProduct>() {
			@Override
			public Predicate toPredicate(Root<ChargingProduct> root, CriteriaQuery<?> query,CriteriaBuilder criteriaBuilder) {
				
				if (ObjectUtils.isEmpty(filter.getUserId())) {
					return criteriaBuilder.conjunction();
				}
				Join<ChargingProduct, UserDetail> userDetail = root.join("userDetail");
				return criteriaBuilder.equal(userDetail.get("id"), filter.getUserId());
			}
		};
	}
	
	
	public static Specification<ChargingProduct> filterLiveChargingProduct() {
		return new Specification<ChargingProduct>() {
			@Override
			public Predicate toPredicate(Root<ChargingProduct> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("isLive")), "Y");
			}
		};
	}

	
	// VehicleMaintenanceRecord record spec
	
	public static Specification<VehicleMaintenanceRecord> filterMaintenanceRecordByStatus(FilterCriteria filter) {
		return new Specification<VehicleMaintenanceRecord>() {
			@Override
			public Predicate toPredicate(Root<VehicleMaintenanceRecord> root, CriteriaQuery<?> query,CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}

	public static Specification<VehicleMaintenanceRecord> filterMaintenanceRecordByName(FilterCriteria filter) {
		return new Specification<VehicleMaintenanceRecord>() {
			@Override
			public Predicate toPredicate(Root<VehicleMaintenanceRecord> root, CriteriaQuery<?> query,CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),"%" + filter.getFilterByName().toUpperCase() + "%");
				// return criteriaBuilder.like(root.get("name"), "%" + filter.getFilterByName()
				// + "%");
			}
		};
	}
	
	// Services - Services menu in website and in admin UI
	
	public static Specification<ServiceProviders> filterServiceProvidersByName(FilterCriteria filter) {
		return new Specification<ServiceProviders>() {
			@Override
			public Predicate toPredicate(Root<ServiceProviders> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}
	
	public static Specification<ServiceProviders> filterServiceProvidersByStatus(FilterCriteria filter) {
		return new Specification<ServiceProviders>() {
			@Override
			public Predicate toPredicate(Root<ServiceProviders> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}
	
	public static Specification<ServiceProviders> filterServiceProvidersByType(FilterCriteria filter) {
		return new Specification<ServiceProviders>() {
			@Override
			public Predicate toPredicate(Root<ServiceProviders> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
			
				return criteriaBuilder.equal(root.get("type"), filter.getType());
			}
		};
	}
	
	// Service  Request 
	
	public static Specification<LogisticRequest> filterLogisticRequestByName(FilterCriteria filter) {
		return new Specification<LogisticRequest>() {
			@Override
			public Predicate toPredicate(Root<LogisticRequest> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}
}
