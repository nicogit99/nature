package com.it.naturlink.db;

import com.it.naturlink.Utils.CalcoloRaccolti;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Allevamento {

    private String tipoAnimale;
    private int numeroAnimali;
    private double produzioneAnnua;//in litri

}
