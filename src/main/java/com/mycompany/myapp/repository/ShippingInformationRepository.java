package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ShippingInformation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShippingInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShippingInformationRepository
    extends JpaRepository<ShippingInformation, Long>, JpaSpecificationExecutor<ShippingInformation> {}
