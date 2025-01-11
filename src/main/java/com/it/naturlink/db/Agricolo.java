package com.it.naturlink.db;

import com.it.naturlink.naturlink.model.Prodotto;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.mapstruct.Mapper;

import java.math.BigDecimal;


@Getter
@Setter
@Table(name = "AGRICOLO")
@Entity
@NoArgsConstructor
public class Agricolo extends Prodotto {

    public Agricolo(String nome, String tipo, Integer quantita, Integer prezzo, Integer superficie, Integer giorniCrescita) {
        super(nome, tipo, quantita, prezzo, superficie, giorniCrescita);
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

    @Min(1)
    @Column(name = "quantita")
    @Override
    public Integer getQuantita() {
        return super.getQuantita();
    }

    @Column(name = "prezzo")
    @Override
    @Min(1) @Max(10)
    public Integer getPrezzo() {
        return super.getPrezzo();
    }

    @Column(name = "superficie")
    @Override
    @Min(1)
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
