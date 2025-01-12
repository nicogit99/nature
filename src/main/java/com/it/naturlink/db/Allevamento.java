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


    public Allevamento(String tipo, Integer prezzo) {
        super(tipo, prezzo);
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
    @Min(1) @Max(10000)
    @Override
    public Integer getPrezzo() {
        return super.getPrezzo();
    }


}
