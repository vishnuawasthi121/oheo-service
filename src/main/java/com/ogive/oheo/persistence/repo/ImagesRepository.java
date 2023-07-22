package com.ogive.oheo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.persistence.entities.Images;

public interface ImagesRepository extends CrudRepository<Images, Long> , JpaSpecificationExecutor<Images> {

	@Modifying
	void deleteByChargingProductId(Long productId);
	
	@Modifying
	void deleteByProductId(Long productId);
	
	@Modifying
	void deleteByProductIdAndImageTypeIn(Long productId, List<ImageType> imageTypes);
	
	
	@Modifying
	void deleteByLeaseDetailAndImageTypeIn(Long leaseId, List<ImageType> imageTypes);
}
