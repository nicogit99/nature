package com.it.naturlink.controller;

import com.it.naturlink.Utils.Production;
import com.it.naturlink.Utils.Weather;
import com.it.naturlink.db.mapper.AgricoloMapper;
import com.it.naturlink.naturlink.api.ProdottiApiDelegate;
import com.it.naturlink.naturlink.model.Prodotto;
import com.it.naturlink.service.AgricoloService;
import com.it.naturlink.service.AgricoloServiceImpl;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/dashboard")
public class AgricoloController {

    @Autowired
    private AgricoloServiceImpl agricoloService;

    @Autowired
    private Weather weather;  // Reuse the same weather object

    // Logging
    private static final Logger logger = LoggerFactory.getLogger(AgricoloController.class);

    @GetMapping("/agricoltura")
    public ModelAndView getAllProductsPage() {
        Integer giorniCrescita;
        float superficie;
        double tonnellate;

        ModelAndView modelAndView = new ModelAndView("agricolo");
        List<Double> tonnellateList = new ArrayList<>();
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();

        for (Prodotto p : prodotti.getBody()) {
            superficie = AgricoloMapper.INSTANCE.toAgricolo(p).getSuperficie();
            giorniCrescita = AgricoloMapper.INSTANCE.toAgricolo(p).getGiorniCrescita();
            tonnellate = Production.calcolaProduzioneAgricola(giorniCrescita, weather.getTemperatura(), weather.getPrecipitazioni(), weather.getUmidita(), superficie);
            tonnellateList.add(tonnellate);
        }

        modelAndView.addObject("tonnellateList", tonnellateList);
        modelAndView.addObject("agri", prodotti.getBody());

        if (prodotti.getStatusCode().is2xxSuccessful() && prodotti.hasBody()) {
            return modelAndView;
        } else {
            logger.error("Failed to fetch products or empty response from agricoloService.");
            modelAndView.setViewName("errore");
            modelAndView.addObject("errore", "Impossibile ottenere i dati dei prodotti.");
            return modelAndView;
        }
    }

    // Endpoint to serve the fragment content
    @GetMapping("/agricoltura/fragment")
    public ModelAndView getAgricolturaFragment(Model model) {
        List<Double> tonnellateList = new ArrayList<>();
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();

        // Calculate tonnellate for each product and add to the list
        for (Prodotto p : prodotti.getBody()) {
            float superficie = AgricoloMapper.INSTANCE.toAgricolo(p).getSuperficie();
            Integer giorniCrescita = AgricoloMapper.INSTANCE.toAgricolo(p).getGiorniCrescita();
            double tonnellate = Production.calcolaProduzioneAgricola(giorniCrescita, weather.getTemperatura(), weather.getPrecipitazioni(), weather.getUmidita(), superficie);
            tonnellateList.add(tonnellate);
        }

        model.addAttribute("tonnellateList", tonnellateList);
        model.addAttribute("prodotti", prodotti.getBody());

        // Returning the fragment inside ModelAndView, render the fragment view
        ModelAndView modelAndView = new ModelAndView("fragments"); // This will use fragments.html
        return modelAndView;
    }


    @GetMapping("/agricoltura/json")
    @ResponseBody
    public ResponseEntity<List<Prodotto>> getAllProductsJson() {
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();
        List<Double> tonnellateList = new ArrayList<>();

        if (prodotti.getStatusCode().is2xxSuccessful() && prodotti.hasBody()) {
            return ResponseEntity.ok(prodotti.getBody());
        } else {
            logger.error("Failed to fetch products or empty response from agricoloService.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}
