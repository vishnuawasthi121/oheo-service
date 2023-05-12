package com.ogive.oheo.dto.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.persistence.entities.BuyRequest;
import com.ogive.oheo.persistence.entities.ChargingProduct;
import com.ogive.oheo.persistence.entities.Frame;
import com.ogive.oheo.persistence.entities.Images;
import com.ogive.oheo.persistence.entities.Position;
import com.ogive.oheo.persistence.entities.Product;
import com.ogive.oheo.persistence.entities.VehicleMaintenanceRecord;
import com.ogive.oheo.persistence.entities.Widget;

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

	// **************************************************** //

	public static Specification<Position> filterPositionByName(FilterCriteria filter) {
		return new Specification<Position>() {
			@Override
			public Predicate toPredicate(Root<Position> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<Position> filterPositionByStatus(FilterCriteria filter) {
		return new Specification<Position>() {
			@Override
			public Predicate toPredicate(Root<Position> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}

	public static Specification<Frame> filterFrameByName(FilterCriteria filter) {
		return new Specification<Frame>() {
			@Override
			public Predicate toPredicate(Root<Frame> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getFilterByName())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
						"%" + filter.getFilterByName().toUpperCase() + "%");
			}
		};
	}

	public static Specification<Frame> filterFrameByStatus(FilterCriteria filter) {
		return new Specification<Frame>() {
			@Override
			public Predicate toPredicate(Root<Frame> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
			}
		};
	}

	public static Specification<Widget> filterWidgetByName(FilterCriteria filter) {
		return new Specification<Widget>() {
			@Override
			public Predicate toPredicate(Root<Widget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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

	public static Specification<Widget> filterWidgetByStatus(FilterCriteria filter) {
		return new Specification<Widget>() {
			@Override
			public Predicate toPredicate(Root<Widget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("status"), filter.getStatus());
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
				if (ObjectUtils.isEmpty(filter.getStatus())) {
					return criteriaBuilder.conjunction();
				}
				return criteriaBuilder.equal(root.get("email"), filter.getEmail());
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

}
