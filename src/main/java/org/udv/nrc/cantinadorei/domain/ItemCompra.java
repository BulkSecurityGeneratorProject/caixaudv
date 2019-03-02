package org.udv.nrc.cantinadorei.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ItemCompra.
 */
@Entity
@Table(name = "item_compra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ItemCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "quant", nullable = false)
    private Integer quant;

    @ManyToOne
    @JsonIgnoreProperties("itensCompras")
    private Produto produto;

    @ManyToOne
    @JsonIgnoreProperties("itensCompras")
    private Compra compra;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuant() {
        return quant;
    }

    public ItemCompra quant(Integer quant) {
        this.quant = quant;
        return this;
    }

    public void setQuant(Integer quant) {
        this.quant = quant;
    }

    public Produto getProduto() {
        return produto;
    }

    public ItemCompra produto(Produto produto) {
        this.produto = produto;
        return this;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Compra getCompra() {
        return compra;
    }

    public ItemCompra compra(Compra compra) {
        this.compra = compra;
        return this;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
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
        ItemCompra itemCompra = (ItemCompra) o;
        if (itemCompra.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itemCompra.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItemCompra{" +
            "id=" + getId() +
            ", quant=" + getQuant() +
            "}";
    }
}
