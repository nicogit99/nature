package com.it.naturlink.Utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Random;
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Weather {

    private static volatile Weather instance;

    @NotBlank
    @Size(min = 200 ,max = 800)
    private double precipitazioni;
    private double umidita;
    private double temperatura;


//    public Weather (){
//        setTemperatura(10 + (rand.nextDouble() * 21));
//        setUmidita(10 + (rand.nextDouble() * 81));
//        setPrecipitazioni(101 + (rand.nextDouble() * (1000 - 101)));
//    }


}