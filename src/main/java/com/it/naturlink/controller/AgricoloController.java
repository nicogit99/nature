package com.it.naturlink.controller;

import com.it.naturlink.Utils.Weather;
import com.it.naturlink.naturlink.api.ProdottiApiDelegate;
import com.it.naturlink.naturlink.model.Prodotto;
import com.it.naturlink.service.AgricoloService;
import com.it.naturlink.service.AgricoloServiceImpl;
import com.it.naturlink.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/dashboard")
public class AgricoloController  {

    // private final AgricoloService agricoloService;

    @Autowired
    AgricoloServiceImpl agricoloService;

    @Autowired
    WeatherService weatherService;

    @GetMapping("/agricoltura")
    public ModelAndView getAllProducts() {

        Weather w = weatherService.generateRandom();
        ModelAndView modelAndView = new ModelAndView("agricolo");

        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();

        if (prodotti.getStatusCode().is2xxSuccessful() && prodotti.hasBody()) {
            modelAndView.addObject("agri", prodotti.getBody());
            return modelAndView;
        } else {

            modelAndView.setViewName("errore");  // Imposta una vista di errore (crea errore.html)
            modelAndView.addObject("errore", "Impossibile ottenere i dati dei prodotti.");
            return modelAndView;
        }
    }


//    @GetMapping(value = "/temp")
//    public Weather getAgricoloList() {
//         Weather w=weatherService.generateRandom();
//        log.info(String.valueOf(w.getTemperatura()));
//        return w;
//    }


}
