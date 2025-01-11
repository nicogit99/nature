package com.it.naturlink.Utils;

public class PerformanceFinance {


    public static double calcolaFatturato(double quantitaVenduta, double prezzoUnitario) {
        return quantitaVenduta * prezzoUnitario;
    }

    // Metodo per calcolare l'EBIT (Profitto operativo)
    public static double calcolaEBIT(double fatturato, double costiProduzione) {
        return fatturato - costiProduzione;
    }

    // Metodo per calcolare l'utile netto
    public static double calcolaUtileNetto(double ebit, double imposte) {
        return ebit - imposte;
    }

    // Metodo per calcolare il margine EBIT
    public static double calcolaMargineEBIT(double ebit, double fatturato) {
        return (ebit / fatturato) * 100;
    }

    // Metodo per calcolare il ROE (Return on Equity)
    public static double calcolaROE(double utileNetto, double capitaleProprio) {
        return (utileNetto / capitaleProprio) * 100;
    }
}
