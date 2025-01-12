package com.it.naturlink.db;

import com.it.naturlink.naturlink.model.Prodotto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Table(name = "AGRICOLO")
@Entity
@NoArgsConstructor
public class Agricolo extends Prodotto {

    public Agricolo(String nome, String tipo, Integer prezzo, Integer superficie, Integer giorniCrescita) {
        super(nome, tipo, prezzo, superficie, giorniCrescita);
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

    @Column(name = "prezzo")
    @Override
    @Min(1) @Max(10)
    public Integer getPrezzo() {
        return super.getPrezzo();
    }

    @Column(name = "superficie")
    @Override
    public Integer getSuperficie() {
        return super.getSuperficie();
    }
    @Column(name = "giorniCrescita")

    @Override
    public Integer getGiorniCrescita() {
        return super.getGiorniCrescita();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
