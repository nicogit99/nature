package com.it.naturlink.Utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
public class CalcoloRaccolti {

    private static  final double pesoTemp = 0.4;
    private  static  final double pesoUmidita = 0.3;
    private static  final double pesoPrecipitazioni = 0.3;
    private  static  final double pesoCrescita = 0.2; // L'importanza dei tempi di crescita

    private static final double tempOttimaleMin = 15.0;
    private  static final double tempOttimaleMax = 25.0;
    private static final double umiditaOttimaleMin = 50.0;
    private  static final double umiditaOttimaleMax = 80.0;
    private static final double precipitazioniOttimaliMin = 500.0; // mm annuali
    private static final double precipitazioniOttimaliMax = 1000.0;


    public static double calcolaRaccolto(double temp, double umidita, double precipitazioni, double giorniCrescita, double superficie) {

        double impattoTemp;
        double impattoUmidita;
        double impattoPrecipitazioni;
        double impattoCrescita;

        if (temp < tempOttimaleMin) {
            impattoTemp = (temp / tempOttimaleMin) * 0.5; // Ridotto se sotto il minimo
        } else if (temp > tempOttimaleMax) {
            impattoTemp = (tempOttimaleMax / temp) * 0.5; // Ridotto se sopra il massimo
        } else {
            impattoTemp = 1.0; // Ottimale se dentro l'intervallo
        }


        if (umidita < umiditaOttimaleMin) {
            impattoUmidita = (umidita / umiditaOttimaleMin) * 0.5; // Ridotto se sotto il minimo
        } else if (umidita > umiditaOttimaleMax) {
            impattoUmidita = (umiditaOttimaleMax / umidita) * 0.5; // Ridotto se sopra il massimo
        } else {
            impattoUmidita = 1.0; // Ottimale se dentro l'intervallo
        }


        if (precipitazioni < precipitazioniOttimaliMin) {
            impattoPrecipitazioni = (precipitazioni / precipitazioniOttimaliMin) * 0.5; // Ridotto se sotto il minimo
        } else if (precipitazioni > precipitazioniOttimaliMax) {
            impattoPrecipitazioni = (precipitazioniOttimaliMax / precipitazioni) * 0.5; // Ridotto se sopra il massimo
        } else {
            impattoPrecipitazioni = 1.0; // Ottimale se dentro l'intervallo
        }

        // Calcolo dell'impatto dei giorni di crescita (minori giorni, maggiore produzione)
        if (giorniCrescita <= 90) {
            impattoCrescita = 1.0; // Coltura a rapida crescita
        } else if (giorniCrescita > 150) {
            impattoCrescita = 0.7; // Coltura a crescita lenta
        } else {
            impattoCrescita = 0.9; // Coltura a crescita media
        }

        // Calcolo complessivo del raccolto per ettaro
        double raccoltoPerEttaro = (impattoTemp * pesoTemp) + (impattoUmidita * pesoUmidita) + (impattoPrecipitazioni * pesoPrecipitazioni) + (impattoCrescita * pesoCrescita);

        // Aggiungere un fattore legato al tipo di coltura
        // ad esempio: 1.2 per una coltura particolarmente resistente, 0.8 per una coltura più sensibile

        // Calcolare la produzione annuale moltiplicando per la superficie in ettari
        double produzioneAnnuale = raccoltoPerEttaro * superficie;

        return produzioneAnnuale;
    }



//    public class Coltura {
//
//        // Proprietà della coltura
//        private double temperatura;
//        private double umidita;
//        private double precipitazioni;
//        private double quantitaRaccolto;
//        private int tempiCrescita;
//
//        // Costruttore della classe
//        public Coltura(double temperatura, double umidita, double precipitazioni, double quantitaRaccolto, int tempiCrescita) {
//            this.temperatura = temperatura;
//            this.umidita = umidita;
//            this.precipitazioni = precipitazioni;
//            this.quantitaRaccolto = quantitaRaccolto;
//            this.tempiCrescita = tempiCrescita;
//        }
//
//        // Metodo per calcolare l'indice di salute della coltura
//        public double calcolaIndiceSalute() {
//            double salute = 100;  // Iniziamo con un punteggio massimo di 100
//
//            // Calcoliamo l'effetto della temperatura (ipotizzando una temperatura ottimale tra 18 e 24 gradi Celsius)
//            if (temperatura < 18 || temperatura > 24) {
//                salute -= Math.abs(temperatura - 21) * 2; // Penalizzazione se la temperatura è fuori intervallo
//            }
//
//            // Calcoliamo l'effetto dell'umidità (ipotizzando una gamma ottimale tra 40% e 60%)
//            if (umidita < 40 || umidita > 60) {
//                salute -= Math.abs(umidita - 50) * 1.5; // Penalizzazione se l'umidità è fuori intervallo
//            }
//
//            // Calcoliamo l'effetto delle precipitazioni (ipotizzando una media annuale ottimale di 800 mm)
//            if (precipitazioni < 600) {
//                salute -= (600 - precipitazioni) * 0.5; // Penalizzazione se le precipitazioni sono insufficienti
//            } else if (precipitazioni > 1000) {
//                salute -= (precipitazioni - 1000) * 0.5; // Penalizzazione se le precipitazioni sono eccessive
//            }
//
//            // Calcoliamo l'effetto sui tempi di crescita (ipotizzando che i tempi di crescita ideali siano tra 90 e 180 giorni)
//            if (tempiCrescita < 90 || tempiCrescita > 180) {
//                salute -= Math.abs(tempiCrescita - 135) * 0.5; // Penalizzazione se i tempi di crescita sono troppo corti o lunghi
//            }
//
//            // Modifichiamo la quantità del raccolto in base all'indice di salute
//            double raccoltoStimato = quantitaRaccolto * (salute / 100);
//
//            // Stampiamo i risultati
//            System.out.println("Indice di salute della coltura: " + salute);
//            System.out.println("Raccolto stimato (in base all'indice di salute): " + raccoltoStimato);
//
//            return salute;
//        }
//
    }






