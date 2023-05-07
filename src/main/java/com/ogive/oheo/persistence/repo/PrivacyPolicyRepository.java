package com.ogive.oheo.persistence.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.entities.PrivacyPolicy;

public interface PrivacyPolicyRepository  extends CrudRepository<PrivacyPolicy, Long>, JpaSpecificationExecutor<PrivacyPolicy>{

	Optional<PrivacyPolicy> findFirstByOrderByCreatedDateDesc();
}
