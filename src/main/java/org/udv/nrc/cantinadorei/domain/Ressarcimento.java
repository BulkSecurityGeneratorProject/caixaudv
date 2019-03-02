package org.udv.nrc.cantinadorei.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Ressarcimento.
 */
@Entity
@Table(name = "ressarcimento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ressarcimento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "valor", nullable = false)
    private Float valor;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @ManyToOne
    @JsonIgnoreProperties("ressarcimentos")
    private SessaoCaixa sessaoCaixa;

    @ManyToOne
    @JsonIgnoreProperties("ressarcimentos")
    private Conta conta;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValor() {
        return valor;
    }

    public Ressarcimento valor(Float valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public Ressarcimento data(LocalDate data) {
        this.data = data;
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public SessaoCaixa getSessaoCaixa() {
        return sessaoCaixa;
    }

    public Ressarcimento sessaoCaixa(SessaoCaixa sessaoCaixa) {
        this.sessaoCaixa = sessaoCaixa;
        return this;
    }

    public void setSessaoCaixa(SessaoCaixa sessaoCaixa) {
        this.sessaoCaixa = sessaoCaixa;
    }

    public Conta getConta() {
        return conta;
    }

    public Ressarcimento conta(Conta conta) {
        this.conta = conta;
        return this;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
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
        Ressarcimento ressarcimento = (Ressarcimento) o;
        if (ressarcimento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ressarcimento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ressarcimento{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", data='" + getData() + "'" +
            "}";
    }
}
