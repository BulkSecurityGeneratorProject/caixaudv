package org.udv.nrc.caixaudv.repository;

import org.udv.nrc.caixaudv.domain.ItemCompra;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemCompra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {
    @Query("select item_compra from ItemCompra item_compra where item_compra.compra.conta.user.login = ?#{principal.username}")
    List<ItemCompra> findByUserIsCurrentUser();
}
