package com.it.naturlink.Utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Production {

    // Metodo per calcolare la produzione agricola in tonnellate
    public static int calcolaProduzioneAgricola(int tempoIniziale, int temperatura, int precipitazioni, int umidita, int superficie) {
        int raccoltoBase = 2;  // Raccolto base per unità di superficie

        // Modifica il raccolto base in funzione del tempo di crescita
        if (tempoIniziale > 100) {
            raccoltoBase = 4;
        } else if (tempoIniziale < 50) {
            raccoltoBase = 2;
        }

        // Modifica del tempo di crescita in base alle condizioni
        double tempoFinale = tempoIniziale;

        if (temperatura > 30) {
            tempoFinale -= tempoIniziale * 0.10;
        } else if (temperatura < 20) {
            tempoFinale += tempoIniziale * 0.15;
        }

        if (precipitazioni > 80) {
            tempoFinale -= tempoIniziale * 0.05;
        } else if (precipitazioni < 20) {
            tempoFinale += tempoIniziale * 0.10;
        }

        if (umidita < 40 || umidita > 80) {
            tempoFinale += tempoIniziale * 0.05;
        }

        // Produzione agricola proporzionale al tempo di crescita finale
        double produzionePerUnità = raccoltoBase * (tempoFinale / tempoIniziale);

        // Moltiplica per la superficie per ottenere la produzione totale
        return (int) (produzionePerUnità * superficie);
    }

    // Metodo per calcolare la produzione del settore pesca in tonnellate
    public static double calcolaProduzionePesca(int temperaturaAcqua, int profondita, int stockPesci) {
        int fattoreTemperatura = (int) ((temperaturaAcqua >= 15 && temperaturaAcqua <= 25) ? 1.0 : 0.8);
        int fattoreProfondita = (int) ((profondita >= 50 && profondita <= 200) ? 1.0 : 0.7);

        // Produzione proporzionale allo stock di pesci e ai fattori
        return stockPesci * fattoreTemperatura * fattoreProfondita;
    }

    // Metodo per calcolare la produzione mineraria in tonnellate
    public static int calcolaProduzioneMineraria(int quantità, int qualitàMinerale, int profondita) {
        double fattoreQualità = qualitàMinerale / 100.0;
        double fattoreProfondita = (profondita <= 100) ? 1.0 : (profondita <= 200) ? 0.9 : 0.8;

        return (int)(quantità * fattoreQualità * fattoreProfondita * 2.0);
    }

    public static int calcolaProduzioneSilvicoltura(int superficie, int tempoDiCrescita, int precipitazioni, int umidita, int temperatura) {
        double coefficienteCrescita = 1.0;

        // Calcolo un coefficiente di crescita basato su precipitazioni, umidità e temperatura
        if (temperatura > 30) {
            coefficienteCrescita -= 0.1;
        } else if (temperatura < 10) {
            coefficienteCrescita -= 0.1;
        }

        if (precipitazioni < 50) {
            coefficienteCrescita -= 0.1;
        } else if (precipitazioni > 200) {
            coefficienteCrescita -= 0.05;
        }

        if (umidita < 30) {
            coefficienteCrescita -= 0.05;
        } else if (umidita > 80) {
            coefficienteCrescita -= 0.05;
        }

        // Calcoliamo la produttività annuale per ettaro
        double produttivitaPerEttaro = (tempoDiCrescita / 10.0) * 0.15;

        // Moltiplichiamo per il coefficiente di crescita e per la superficie per ottenere la produzione totale
        double produzioneTotale = superficie * produttivitaPerEttaro * coefficienteCrescita;

        // Ritorniamo il risultato come intero
        return (int) Math.round(produzioneTotale);
    }

    public static int calcolaProduzioneAllevamentoAnimali(int numeroAnimali, String tipoAnimale) {
        int fattoreTipoAnimale = 1; // Fattore di regolazione per la produzione

        switch (tipoAnimale.toLowerCase()) {
            case "bovino":
                fattoreTipoAnimale = 2;
                break;
            case "suino":
                fattoreTipoAnimale = 1;
                break;
            case "pollame":
                fattoreTipoAnimale = 1;
                break;
            case "ovino":
                fattoreTipoAnimale = 2;
                break;
            default:
                fattoreTipoAnimale = 1;
                break;
        }

        // Calcoliamo la produzione per animale in base a un anno di allevamento
        int produzionePerAnimale = 0;
        if (tipoAnimale.equalsIgnoreCase("bovino")) {
            produzionePerAnimale = 8;  // 8 quintali di carne per bovino in un anno
        } else if (tipoAnimale.equalsIgnoreCase("suino")) {
            produzionePerAnimale = 4;  // 4 quintali di carne per suino in un anno
        } else if (tipoAnimale.equalsIgnoreCase("pollame")) {
            produzionePerAnimale = 2;  // 2 quintali di carne per pollo in un anno
        } else if (tipoAnimale.equalsIgnoreCase("ovino")) {
            produzionePerAnimale = 3;
        }

        // Calcolare la produzione totale moltiplicando per il numero di animali e il fattore di regolazione
        int produzioneTotale = numeroAnimali * produzionePerAnimale * fattoreTipoAnimale;

        return produzioneTotale;
    }


}
