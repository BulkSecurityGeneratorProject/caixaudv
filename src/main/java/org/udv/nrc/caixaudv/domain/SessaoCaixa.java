package org.udv.nrc.caixaudv.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SessaoCaixa.
 */
@Entity
@Table(name = "sessao_caixa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SessaoCaixa implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "valor_atual", nullable = false)
    private Float valorAtual;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @ManyToOne
    @JsonIgnoreProperties("sessoesCaixas")
    private User user;

    @OneToMany(mappedBy = "sessaoCaixa")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Compra> compras = new HashSet<>();
    @OneToMany(mappedBy = "sessaoCaixa")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ressarcimento> ressarcimentos = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValorAtual() {
        return valorAtual;
    }

    public SessaoCaixa valorAtual(Float valorAtual) {
        this.valorAtual = valorAtual;
        return this;
    }

    public void setValorAtual(Float valorAtual) {
        this.valorAtual = valorAtual;
    }

    public LocalDate getData() {
        return data;
    }

    public SessaoCaixa data(LocalDate data) {
        this.data = data;
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public SessaoCaixa user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Compra> getCompras() {
        return compras;
    }

    public SessaoCaixa compras(Set<Compra> compras) {
        this.compras = compras;
        return this;
    }

    public SessaoCaixa addCompras(Compra compra) {
        this.compras.add(compra);
        compra.setSessaoCaixa(this);
        return this;
    }

    public SessaoCaixa removeCompras(Compra compra) {
        this.compras.remove(compra);
        compra.setSessaoCaixa(null);
        return this;
    }

    public void setCompras(Set<Compra> compras) {
        this.compras = compras;
    }

    public Set<Ressarcimento> getRessarcimentos() {
        return ressarcimentos;
    }

    public SessaoCaixa ressarcimentos(Set<Ressarcimento> ressarcimentos) {
        this.ressarcimentos = ressarcimentos;
        return this;
    }

    public SessaoCaixa addRessarcimentos(Ressarcimento ressarcimento) {
        this.ressarcimentos.add(ressarcimento);
        ressarcimento.setSessaoCaixa(this);
        return this;
    }

    public SessaoCaixa removeRessarcimentos(Ressarcimento ressarcimento) {
        this.ressarcimentos.remove(ressarcimento);
        ressarcimento.setSessaoCaixa(null);
        return this;
    }

    public void setRessarcimentos(Set<Ressarcimento> ressarcimentos) {
        this.ressarcimentos = ressarcimentos;
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
        SessaoCaixa sessaoCaixa = (SessaoCaixa) o;
        if (sessaoCaixa.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sessaoCaixa.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SessaoCaixa{" +
            "id=" + getId() +
            ", valorAtual=" + getValorAtual() +
            ", data='" + getData() + "'" +
            "}";
    }
}
