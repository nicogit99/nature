package com.it.naturlink.Utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Production {
    
    // Metodo per calcolare la produzione agricola in tonnellate
    public static double calcolaProduzioneAgricola(double tempoIniziale, double temperatura, double precipitazioni, double umidita, double superficie) {
        double tempoFinale = tempoIniziale;

        // Modifica del tempo di crescita in base alle condizioni
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
        double raccoltoBase = 1.0; // Tonnellate di riferimento
        double produzionePerUnità = raccoltoBase * (tempoFinale / tempoIniziale);

        // Moltiplica per la superficie per ottenere la produzione totale
        return produzionePerUnità * superficie;
    }


    //AGGIUNGI ENTITA SALATO O DOLCE MARE. STOCK PESCI è LA BIOMASSA IM QUELLA ZONA
    // Metodo per calcolare la produzione del settore pesca in tonnellate
    public static double calcolaProduzionePesca(double temperaturaAcqua, double profondita, double stockPesci) {
        // Fattori di regolazione
        double fattoreTemperatura = (temperaturaAcqua >= 15 && temperaturaAcqua <= 25) ? 1.0 : 0.8;
        double fattoreProfondita = (profondita >= 50 && profondita <= 200) ? 1.0 : 0.7;

        // Produzione proporzionale allo stock di pesci e ai fattori
        return stockPesci * fattoreTemperatura * fattoreProfondita / 100.0; // Conversione in tonnellate
    }

    // Metodo per calcolare la produzione mineraria in tonnellate
    public static double calcolaProduzioneMineraria(double oreLavoro, double qualitàMinerale, double profondita) {
        // Fattore basato sulla qualità del minerale
        double fattoreQualità = qualitàMinerale / 100.0;

        // Fattore basato sulla profondità (ad esempio, profondità maggiori riducono l'efficienza)
        double fattoreProfondita = (profondita <= 100) ? 1.0 : (profondita <= 200) ? 0.9 : 0.8;

        // Produzione proporzionale alle ore di lavoro, alla qualità e alla profondità
        return oreLavoro * fattoreQualità * fattoreProfondita * 2.0; // Produzione di 2 tonnellate per ora ideale
    }

    // Metodo per calcolare la produzione del settore silvicoltura in tonnellate
    public static double calcolaProduzioneSilvicoltura(double superficie, double densitaAlberi, double tempoDiCrescita, double condizioniClimatiche) {
        // Coefficiente di crescita in base alla specie e alle condizioni climatiche
        // Supponiamo che la produttività aumenti del 10% per ogni 10 anni di crescita e condizioni ottimali
        double coefficienteCrescita = 1.0;

        // Se le condizioni climatiche sono ottimali (da 0 a 100), coefficienteCrescita sarà 1.0, altrimenti si adatta.
        if (condizioniClimatiche > 80) {
            coefficienteCrescita = 1.2; // Condizioni ideali: aumento del 20%
        } else if (condizioniClimatiche < 40) {
            coefficienteCrescita = 0.8; // Condizioni sfavorevoli: diminuzione del 20%
        }

        // Calcoliamo la produttività annuale per ettaro in base alla densità degli alberi
        double produttivitaPerEttaro = densitaAlberi * (tempoDiCrescita / 10.0) * 0.15; // 0.15 è un valore di base che rappresenta il volume di legname prodotto per albero e per anno di crescita

        // Moltiplichiamo per il coefficiente di crescita e per la superficie per ottenere la produzione totale
        double produzioneTotale = superficie * produttivitaPerEttaro * coefficienteCrescita;

        return produzioneTotale; // La produzione totale in tonnellate
    }


    public static void main(String[] args) {
        // AGRICOLTURA
        double tempoIniziale = 90; // Tempo di crescita in giorni
        double temperatura = 32; // Temperatura in gradi Celsius
        double precipitazioni = 70; // Precipitazioni in mm
        double umidita = 60; // Umidità in percentuale
        double superficieAgricola = 50; // Superficie coltivata in ettari
        double produzioneAgricola = calcolaProduzioneAgricola(tempoIniziale, temperatura, precipitazioni, umidita, superficieAgricola);
        System.out.printf("Produzione agricola stimata: %.2f tonnellate%n", produzioneAgricola);

        // PESCA
        double temperaturaAcqua = 20; // Temperatura dell'acqua in gradi Celsius
        double profonditaPesca = 100; // Profondità in metri
        double stockPesci = 500; // Stock di pesci in tonnellate
        double produzionePesca = calcolaProduzionePesca(temperaturaAcqua, profonditaPesca, stockPesci);
        System.out.printf("Produzione della pesca stimata: %.2f tonnellate%n", produzionePesca);

        // MINERARIO
        double oreLavoro = 40; // Ore di lavoro totali
        double qualitàMinerale = 80; // Percentuale di qualità del minerale
        double profonditaMineraria = 150; // Profondità in metri
        double produzioneMineraria = calcolaProduzioneMineraria(oreLavoro, qualitàMinerale, profonditaMineraria);
        System.out.printf("Produzione mineraria stimata: %.2f tonnellate%n", produzioneMineraria);

        // SILVICOLTURA
        double areaBoschiva = 500; // Area boschiva in ettari
        double densitàAlberi = 50; // Numero di alberi per ettaro
        double tempoDiCrescita = 10; // Tempo di crescita in anni
        double superficieSilvicoltura = 30; // Superficie forestale in ettari
        double produzioneSilvicoltura = calcolaProduzioneSilvicoltura(areaBoschiva, densitàAlberi, tempoDiCrescita, superficieSilvicoltura);
        System.out.printf("Produzione silvicoltura stimata: %.2f tonnellate%n", produzioneSilvicoltura);
    }
}
