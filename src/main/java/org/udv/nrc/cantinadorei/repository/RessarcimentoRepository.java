package org.udv.nrc.cantinadorei.repository;

import org.udv.nrc.cantinadorei.domain.Ressarcimento;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ressarcimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RessarcimentoRepository extends JpaRepository<Ressarcimento, Long> {
    @Query("select res from Ressarcimento res where res.conta.user.login = ?#{principal.username}")
    List<Ressarcimento> findByUserIsCurrentUser();
}
