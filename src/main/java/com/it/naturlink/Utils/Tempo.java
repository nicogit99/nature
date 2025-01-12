package com.it.naturlink.Utils;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Component
@Scope("singleton")
public class Tempo {

    Random rand = new Random();

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
        setPrecipitazioni(rand.nextInt(601) + 200);  // 200 - 800
        setUmidita(rand.nextInt(41) + 40);  // 40 - 80
        setTemperatura(rand.nextInt(26) + 10);  // 10 - 35

        this.datiPronti = true;
    }

    public boolean areDatiPronti() {
        return datiPronti;
    }

    @PostConstruct
    @Scheduled(fixedRate = 4000)
    public void creaDinuovo() {
        Tempo newTempo = new Tempo();
        // Genera nuovi dati meteo periodicamente
    }
}
