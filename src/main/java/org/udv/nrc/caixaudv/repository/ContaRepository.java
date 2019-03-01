package org.udv.nrc.caixaudv.repository;

import org.udv.nrc.caixaudv.domain.Conta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Conta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    @Query("select conta from Conta conta where conta.user.login = ?#{principal.username}")
    Conta findByUserIsCurrentUser();
}
