package org.udv.nrc.cantinadorei.repository;

import org.udv.nrc.cantinadorei.domain.SessaoCaixa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SessaoCaixa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessaoCaixaRepository extends JpaRepository<SessaoCaixa, Long> {

    @Query("select sessao_caixa from SessaoCaixa sessao_caixa where sessao_caixa.user.login = ?#{principal.username}")
    List<SessaoCaixa> findByUserIsCurrentUser();

}
