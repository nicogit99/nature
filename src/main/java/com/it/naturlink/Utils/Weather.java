package com.it.naturlink.Utils;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
public class Weather {

    Random rand = new Random();

    @DecimalMin("200.0")
    @DecimalMax("800.0")
    private double precipitazioni;

    @DecimalMin("40.0")
    @DecimalMax("80.0")
    private double umidita;

    @DecimalMin("10.0")
    @DecimalMax("35.0")
    private double temperatura;

    private boolean datiPronti = false; // Stato che indica se i dati meteo sono pronti

    public Weather() {
        setTemperatura(10 + (rand.nextDouble() * 21));  // 10 - 30 Celsius
        setUmidita(10 + (rand.nextDouble() * 81));  // 10 - 90% umidità
        setPrecipitazioni(101 + (rand.nextDouble() * (1000 - 101)));  // 101 - 1000 mm di precipitazioni
        this.datiPronti = true;
    }

    public boolean areDatiPronti() {
        return datiPronti;
    }

    @PostConstruct
    @Scheduled(fixedRate = 5000)
    public void creaDinuovo() {
        Weather newWeather = new Weather(); // Genera nuovi dati meteo periodicamente
    }
}
