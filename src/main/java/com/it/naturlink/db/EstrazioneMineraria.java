package com.it.naturlink.db;


import com.it.naturlink.naturlink.model.Minerale;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "MINERALI")
@Entity
@NoArgsConstructor
public class EstrazioneMineraria extends Minerale {

    public EstrazioneMineraria(String nome, String tipo, Integer prezzo, Integer profondita, Integer purezza) {
        super(nome, tipo, prezzo, profondita, purezza);
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
    @SequenceGenerator(name = "id_sequence", sequenceName = "sequenza_id", allocationSize = 1)
    @Id
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "nome")
    @Override
    public String getNome() {
        return super.getNome();
    }

    @Column(name = "tipo")
    @Override
    public String getTipo() {
        return super.getTipo();
    }

    @Column(name = "quantita")
    @Override
    public Integer getQuantita() {
        return super.getQuantita();
    }

    @Column(name = "prezzo")
    @Min(1) @Max(500)
    @Override
    public Integer getPrezzo() {
        return super.getPrezzo();
    }

    @Column(name = "profondita")
    @Min(1) @Max(1000)
    @Override
    public Integer getProfondita() {
        return super.getProfondita();
    }

    @Column(name = "purezza")
    @Min(1) @Max(99)
    @Override
    public Integer getPurezza() {
        return super.getPurezza();
    }
}
