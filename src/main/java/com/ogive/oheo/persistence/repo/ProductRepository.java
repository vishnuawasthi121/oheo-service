package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.persistence.entities.Product;

public interface ProductRepository  extends CrudRepository<Product, Long>, JpaSpecificationExecutor<Product>{

	List<Object[]>	dropDownLive(String isLive);
	
	List<Object[]>	dropDownIfAvailableForLease(StatusCode status);
	
	List<Object[]>	fetchProductDropDownByVehicleType(Long userId,Long vehicleTypeId,StatusCode status);
	
	List<Object[]> fetchEVProductsDropDown (String vehicleFuelTypeName, Long vehicleTypeId);
	
	
	Product findProductByUserIdAndId(Long userId, Long id);
	
	@Modifying
	void deleteByProductId(long productId);
	
	@Modifying
	void upadateVehicleType(Long toVehicleTypeId,Long fromVehicleTypeId);
	
	@Modifying
	void upadateSetAvailableForLease(String availableForLease, Long id);
}
