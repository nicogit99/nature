package com.it.naturlink.db;

import com.it.naturlink.naturlink.model.Prodotto;
import jakarta.persistence.*;
import lombok.*;
import org.mapstruct.Mapper;



@AllArgsConstructor

@Mapper
@Table(name = "AGRICOLO")
@Entity
public class Agricolo extends Prodotto {


    public Agricolo(String nome, String tipo, Integer quantita, Float prezzo, Float superficie) {
        super(nome, tipo, quantita, prezzo, superficie);
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
    @Override
    public Float getPrezzo() {
        return super.getPrezzo();
    }

    @Column(name = "superfice")
    @Override
    public Float getSuperficie() {
        return super.getSuperficie();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
