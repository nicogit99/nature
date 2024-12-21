package com.it.naturlink.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sivicoltura {

    private String tipoForesta;
    private double superficie;  // in ettari
    private double produzioneLegnoAnnuale;  // in metri cubi
}
