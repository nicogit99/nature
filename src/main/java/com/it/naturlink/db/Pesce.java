package com.it.naturlink.db;

import com.it.naturlink.naturlink.model.Pesca;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "PESCE")
@Entity
@NoArgsConstructor
public class Pesce extends Pesca {

    public Pesce(String tipo, Integer stockPesce, Integer profondita, Integer prezzo) {
        super(tipo, stockPesce, profondita, prezzo);
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


    @Column(name = "stockpesce")
    @Override
    @Min(1) @Max(400)
    public Integer getStockPesce() {
        return super.getStockPesce();
    }

    @Column(name = "profondita")
    @Override
    @Min(1)
    public Integer getProfondita() {
        return super.getProfondita();
    }

    @Column(name = "prezzo")
    @Override
    @Min(1) @Max(10)
    public Integer getPrezzo() {
        return super.getPrezzo();
    }
}
