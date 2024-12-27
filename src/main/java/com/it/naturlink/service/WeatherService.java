//package com.it.naturlink.service;
//
//import com.it.naturlink.Utils.Weather;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import java.util.Random;
//@Service
//public class WeatherService {
//    private final Random random=new Random();
//
//    public void generateRandom(){
//         Weather w=new Weather(umidita(), precipitazioni(),temperatura());
//    }
//
//    private double umidita() {
//        return (10 + (random.nextDouble() * 81));
//    }
//
//    private double precipitazioni() {
//        return (101 + (random.nextDouble() * (1000 - 101)));
//    }
//
//    private double temperatura() {
//        return(10 + (random.nextDouble() * 21));
//    }
//
//
//}
