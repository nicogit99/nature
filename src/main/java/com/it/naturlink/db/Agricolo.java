package com.it.naturlink.db;

import com.it.naturlink.naturlink.model.Prodotto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mapstruct.Mapper;

@Setter
@Entity
@Table(name = "AGRICOLO")
@AllArgsConstructor
@ToString
@Mapper
public class Agricolo extends Prodotto {


    public Agricolo(String nome, String tipo, Integer quantita, Float prezzo, Float superficie) {
        super(nome, tipo, quantita, prezzo, superficie);
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public String getTipo() {
        return super.getTipo();
    }

    @Override
    public Integer getQuantita() {
        return super.getQuantita();
    }

    @Override
    public Float getPrezzo() {
        return super.getPrezzo();
    }

    @Override
    public Float getSuperficie() {
        return super.getSuperficie();
    }
}
