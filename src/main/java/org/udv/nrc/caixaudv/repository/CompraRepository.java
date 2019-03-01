package org.udv.nrc.caixaudv.repository;

import org.udv.nrc.caixaudv.domain.Compra;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Compra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query("select compra from Compra compra where compra.conta.user.login = ?#{principal.username}")
    List<Compra> findByUserIsCurrentUser();
}
