package org.udv.nrc.caixaudv.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.udv.nrc.caixaudv.domain.enumeration.NivelPermissao;

/**
 * A Conta.
 */
@Entity
@Table(name = "conta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "saldo_atual", nullable = false)
    private Float saldoAtual;

    @NotNull
    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_permissao", nullable = false)
    private NivelPermissao nivelPermissao;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "conta")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ressarcimento> ressarcimentos = new HashSet<>();
    @OneToMany(mappedBy = "conta")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Compra> compras = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getSaldoAtual() {
        return saldoAtual;
    }

    public Conta saldoAtual(Float saldoAtual) {
        this.saldoAtual = saldoAtual;
        return this;
    }

    public void setSaldoAtual(Float saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public Conta dataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
        return this;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public NivelPermissao getNivelPermissao() {
        return nivelPermissao;
    }

    public Conta nivelPermissao(NivelPermissao nivelPermissao) {
        this.nivelPermissao = nivelPermissao;
        return this;
    }

    public void setNivelPermissao(NivelPermissao nivelPermissao) {
        this.nivelPermissao = nivelPermissao;
    }

    public User getUser() {
        return user;
    }

    public Conta user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Ressarcimento> getRessarcimentos() {
        return ressarcimentos;
    }

    public Conta ressarcimentos(Set<Ressarcimento> ressarcimentos) {
        this.ressarcimentos = ressarcimentos;
        return this;
    }

    public Conta addRessarcimentos(Ressarcimento ressarcimento) {
        this.ressarcimentos.add(ressarcimento);
        ressarcimento.setConta(this);
        return this;
    }

    public Conta removeRessarcimentos(Ressarcimento ressarcimento) {
        this.ressarcimentos.remove(ressarcimento);
        ressarcimento.setConta(null);
        return this;
    }

    public void setRessarcimentos(Set<Ressarcimento> ressarcimentos) {
        this.ressarcimentos = ressarcimentos;
    }

    public Set<Compra> getCompras() {
        return compras;
    }

    public Conta compras(Set<Compra> compras) {
        this.compras = compras;
        return this;
    }

    public Conta addCompras(Compra compra) {
        this.compras.add(compra);
        compra.setConta(this);
        return this;
    }

    public Conta removeCompras(Compra compra) {
        this.compras.remove(compra);
        compra.setConta(null);
        return this;
    }

    public void setCompras(Set<Compra> compras) {
        this.compras = compras;
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
        Conta conta = (Conta) o;
        if (conta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Conta{" +
            "id=" + getId() +
            ", saldoAtual=" + getSaldoAtual() +
            ", dataAbertura='" + getDataAbertura() + "'" +
            ", nivelPermissao='" + getNivelPermissao() + "'" +
            "}";
    }
}
