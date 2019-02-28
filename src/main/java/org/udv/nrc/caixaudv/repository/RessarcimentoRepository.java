package org.udv.nrc.caixaudv.repository;

import org.udv.nrc.caixaudv.domain.Ressarcimento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ressarcimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RessarcimentoRepository extends JpaRepository<Ressarcimento, Long> {

}
