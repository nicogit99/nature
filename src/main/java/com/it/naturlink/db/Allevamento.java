package com.it.naturlink.db;

import com.it.naturlink.naturlink.model.Animale;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "ALLEVAMENTO")
@Entity
@NoArgsConstructor
public class Allevamento extends Animale {

    public Allevamento(String tipo, Integer prezzo, Integer quantita) {
        super(tipo, prezzo, quantita);
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
    @SequenceGenerator(name = "id_sequence", sequenceName = "sequenza_id_allevamento", allocationSize = 1)
    @Id
    @Override
    public Integer getId() {
        return super.getId();
    }


    @Column(name = "tipo")
    @Override
    public String getTipo() {
        return super.getTipo();
    }

    @Column(name = "prezzo")
    @Min(1) @Max(10000)
    @Override
    public Integer getPrezzo() {
        return super.getPrezzo();
    }

    @Column(name = "quantita")
    @Min(1) @Max(15)
    @Override
    public Integer getQuantita() {
        return super.getQuantita();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
