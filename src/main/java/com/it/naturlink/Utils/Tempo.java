package com.it.naturlink.Utils;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Component
@Scope("singleton")
public class Tempo {

    private Random rand = new Random();

    private List<Integer> valori = new ArrayList<>();

    @Min(200)
    @Max(800)
    private int precipitazioni;

    @Min(40)
    @Max(80)
    private int umidita;

    @Min(10)
    @Max(35)
    private int temperatura;

    private boolean datiPronti = false; // Stato che indica se i dati meteo sono pronti

    public Tempo() {
        aggiornaValoriMeteo();
        this.datiPronti = true;
    }

    @PostConstruct
    public void init() {
        // Imposta i valori iniziali
        aggiornaValoriMeteo();
    }

    // Metodo per aggiornare i valori del tempo
    @Scheduled(fixedRate = 20000) // Esegui ogni 5 secondi (5000 millisecondi)
    public void aggiornaValoriMeteo() {
        setPrecipitazioni(rand.nextInt(601) + 200);  // 200 - 800
        setUmidita(rand.nextInt(41) + 40);  // 40 - 80
        setTemperatura(rand.nextInt(26) + 10);  // 10 - 35
        valori.clear();
        valori.add(precipitazioni);
        valori.add(umidita);
        valori.add(temperatura);
    }

    public boolean areDatiPronti() {
        return datiPronti;
    }
}