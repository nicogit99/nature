package com.it.naturlink.controller;

import com.it.naturlink.Utils.Weather;
import com.it.naturlink.naturlink.api.ProdottiApiDelegate;
import com.it.naturlink.naturlink.model.Prodotto;
import com.it.naturlink.service.AgricoloService;
import com.it.naturlink.service.AgricoloServiceImpl;
import com.it.naturlink.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
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
    public ModelAndView getAllProductsPage() {
        // Genera informazioni meteo (opzionale)
        Weather w = weatherService.generateRandom();

        // Crea ModelAndView per la pagina agricola
        ModelAndView modelAndView = new ModelAndView("agricolo");

        // Recupera la lista dei prodotti tramite il servizio agricolo
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();

        if (prodotti.getStatusCode().is2xxSuccessful() && prodotti.hasBody()) {
            // Aggiungi i dati al modello per Thymeleaf
            modelAndView.addObject("agri", prodotti.getBody());
        } else {
            // In caso di errore nei dati
            modelAndView.setViewName("errore");
            modelAndView.addObject("errore", "Impossibile ottenere i dati dei prodotti.");
        }

        return modelAndView;
    }

    // Metodo per la risposta AJAX in formato JSON
    @GetMapping("/agricoltura/json")
    @ResponseBody
    public ResponseEntity<List<Prodotto>> getAllProductsJson() {
        // Recupera la lista dei prodotti tramite il servizio agricolo
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();

        if (prodotti.getStatusCode().is2xxSuccessful() && prodotti.hasBody()) {
            // Restituisci i dati in formato JSON per la richiesta AJAX
            return ResponseEntity.ok(prodotti.getBody());
        } else {
            // In caso di errore, restituisci una risposta di errore (status 500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Vuoto o personalizza il messaggio
        }
    }


//    @GetMapping(value = "/temp")
//    public Weather getAgricoloList() {
//         Weather w=weatherService.generateRandom();
//        log.info(String.valueOf(w.getTemperatura()));
//        return w;
//    }


}
