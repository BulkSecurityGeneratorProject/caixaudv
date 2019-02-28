package org.udv.nrc.caixaudv.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Compra.
 */
@Entity
@Table(name = "compra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "valor_total", nullable = false)
    private Float valorTotal;

    @NotNull
    @Column(name = "nome_solic", nullable = false)
    private String nomeSolic;

    @ManyToOne
    @JsonIgnoreProperties("compras")
    private SessaoCaixa sessaoCaixa;

    @ManyToOne
    @JsonIgnoreProperties("compras")
    private Conta conta;

    @OneToMany(mappedBy = "compra")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ItemCompra> itensCompras = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValorTotal() {
        return valorTotal;
    }

    public Compra valorTotal(Float valorTotal) {
        this.valorTotal = valorTotal;
        return this;
    }

    public void setValorTotal(Float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNomeSolic() {
        return nomeSolic;
    }

    public Compra nomeSolic(String nomeSolic) {
        this.nomeSolic = nomeSolic;
        return this;
    }

    public void setNomeSolic(String nomeSolic) {
        this.nomeSolic = nomeSolic;
    }

    public SessaoCaixa getSessaoCaixa() {
        return sessaoCaixa;
    }

    public Compra sessaoCaixa(SessaoCaixa sessaoCaixa) {
        this.sessaoCaixa = sessaoCaixa;
        return this;
    }

    public void setSessaoCaixa(SessaoCaixa sessaoCaixa) {
        this.sessaoCaixa = sessaoCaixa;
    }

    public Conta getConta() {
        return conta;
    }

    public Compra conta(Conta conta) {
        this.conta = conta;
        return this;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Set<ItemCompra> getItensCompras() {
        return itensCompras;
    }

    public Compra itensCompras(Set<ItemCompra> itemCompras) {
        this.itensCompras = itemCompras;
        return this;
    }

    public Compra addItensCompra(ItemCompra itemCompra) {
        this.itensCompras.add(itemCompra);
        itemCompra.setCompra(this);
        return this;
    }

    public Compra removeItensCompra(ItemCompra itemCompra) {
        this.itensCompras.remove(itemCompra);
        itemCompra.setCompra(null);
        return this;
    }

    public void setItensCompras(Set<ItemCompra> itemCompras) {
        this.itensCompras = itemCompras;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Compra compra = (Compra) o;
        if (compra.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), compra.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Compra{" +
            "id=" + getId() +
            ", valorTotal=" + getValorTotal() +
            ", nomeSolic='" + getNomeSolic() + "'" +
            "}";
    }
}
