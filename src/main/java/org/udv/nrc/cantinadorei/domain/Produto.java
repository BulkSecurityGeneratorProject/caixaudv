package org.udv.nrc.cantinadorei.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Produto.
 */
@Entity
@Table(name = "produto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "preco", nullable = false)
    private Float preco;

    @Size(max = 191)
    @Column(name = "descricao", length = 191)
    private String descricao;

    @OneToMany(mappedBy = "produto")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ItemCompra> itensCompras = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Produto nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getPreco() {
        return preco;
    }

    public Produto preco(Float preco) {
        this.preco = preco;
        return this;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public Produto descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<ItemCompra> getItensCompras() {
        return itensCompras;
    }

    public Produto itensCompras(Set<ItemCompra> itemCompras) {
        this.itensCompras = itemCompras;
        return this;
    }

    public Produto addItensCompra(ItemCompra itemCompra) {
        this.itensCompras.add(itemCompra);
        itemCompra.setProduto(this);
        return this;
    }

    public Produto removeItensCompra(ItemCompra itemCompra) {
        this.itensCompras.remove(itemCompra);
        itemCompra.setProduto(null);
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
        Produto produto = (Produto) o;
        if (produto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), produto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Produto{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", preco=" + getPreco() +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
