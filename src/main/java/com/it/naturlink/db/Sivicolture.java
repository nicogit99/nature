package com.it.naturlink.db;

import com.it.naturlink.naturlink.model.Sivicoltura;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "SIVICOLTURA")
@Entity
@NoArgsConstructor
public class Sivicolture extends Sivicoltura {





    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
    @SequenceGenerator(name = "id_sequence", sequenceName = "sequenza_id", allocationSize = 1)
    @Id
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    @Column(name = "nome")
    public String getNome() {
        return super.getNome();
    }

    @Override
    @Column(name = "tipo")
    public String getTipo() {
        return super.getTipo();
    }


    @Column(name = "prezzo")
    @Override
    @Min(1) @Max(100)
    public Integer getPrezzo() {
        return super.getPrezzo();
    }


    @Column(name = "superficie")
    @Override
    @Min(1) @Max(10)
    public Integer getSuperficie() {
        return super.getSuperficie();
    }

    @Override
    @Column(name = "giorniCrescita")
    public Integer getGiorniCrescita() {
        return super.getGiorniCrescita();
    }
}
