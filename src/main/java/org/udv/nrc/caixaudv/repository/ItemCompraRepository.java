package org.udv.nrc.caixaudv.repository;

import org.udv.nrc.caixaudv.domain.ItemCompra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemCompra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {

}
