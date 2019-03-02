package org.udv.nrc.cantinadorei.repository;

import org.udv.nrc.cantinadorei.domain.Compra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Compra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

}
