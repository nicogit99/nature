package com.it.naturlink.controller;

import com.it.naturlink.Utils.Production;
import com.it.naturlink.Utils.Weather;
import com.it.naturlink.db.mapper.AgricoloMapper;
import com.it.naturlink.naturlink.model.Prodotto;
import com.it.naturlink.service.AgricoloServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "")
public class AgricoloController {

    @Autowired
    private AgricoloServiceImpl agricoloService;

    @Autowired
    private Weather weather;  // Reuse the same weather object

    private List<Integer> GuadagnoPerProdotti;

    @GetMapping("")
    public ModelAndView homepage() {
        return new ModelAndView("/agricolo/agricolo");
    }



    @GetMapping("/agricoltura/datatable-framments")
    public ResponseEntity<Map<String, Object>> getTableFragment() {
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int guadagnoperProdotto = 0;
        int totaleprodotti = 0;

        for (Prodotto p : prodotti.getBody()) {
            int superficie = AgricoloMapper.INSTANCE.toAgricolo(p).getSuperficie();
            Integer giorniCrescita = AgricoloMapper.INSTANCE.toAgricolo(p).getGiorniCrescita();

            int tonnellate = Production.calcolaProduzioneAgricola(giorniCrescita, weather.getTemperatura(), weather.getPrecipitazioni(), weather.getUmidita(), superficie);
            tonnellateList.add(tonnellate);
            guadagnoperProdotto = (AgricoloMapper.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
//            totaleprodotti += (AgricoloMapper.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
            tonnellateGuadagno.add(guadagnoperProdotto);
            totaleprodotti+=(AgricoloMapper.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
        }

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);

        response.put("tonnellateList", tonnellateList);
        response.put("prodotti", prodotti.getBody());

        // Return ResponseEntity with data and HTTP status

        return ResponseEntity.ok(response);
    }


















    @GetMapping("/agricoltura/donutchart-fragments")
    public ModelAndView getDonutFragment(Model model) {
        // Otteniamo la lista dei prodotti
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();

        // Inizializzazione delle liste e variabili
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int totaleprodotti = 0;

        // Calcoliamo la produzione per ciascun prodotto
        for (Prodotto p : prodotti.getBody()) {
            int superficie = AgricoloMapper.INSTANCE.toAgricolo(p).getSuperficie();
            Integer giorniCrescita = AgricoloMapper.INSTANCE.toAgricolo(p).getGiorniCrescita();

            int tonnellate = Production.calcolaProduzioneAgricola(
                    giorniCrescita, weather.getTemperatura(), weather.getPrecipitazioni(), weather.getUmidita(), superficie
            );
            tonnellateList.add(tonnellate);

            // Guadagno per prodotto
            int guadagnoperProdotto = (AgricoloMapper.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
            totaleprodotti += guadagnoperProdotto;
            tonnellateGuadagno.add(guadagnoperProdotto);
        }

        // Inizializzazione delle variabili per i guadagni totali per tipo di prodotto
        int frutta = 0;
        int verdura = 0;
        int ortaggi = 0;

        // Classificare i guadagni in base al tipo di prodotto
        for (int i = 0; i < tonnellateGuadagno.size(); i++) {
            if (i <= 5) {
                frutta += tonnellateGuadagno.get(i);
            } else if (i > 5 && i <= 11) {
                verdura += tonnellateGuadagno.get(i);
            } else {
                ortaggi += tonnellateGuadagno.get(i);
            }
        }

        // Calcoliamo le percentuali per ciascun tipo di prodotto
        double percentualeFrutta = (totaleprodotti > 0) ? ((double) frutta / totaleprodotti) * 100 : 0;
        double percentualeVerdura = (totaleprodotti > 0) ? ((double) verdura / totaleprodotti) * 100 : 0;
        double percentualeOrtaggi = (totaleprodotti > 0) ? ((double) ortaggi / totaleprodotti) * 100 : 0;

        // Aggiungiamo le percentuali al modello per la visualizzazione
        model.addAttribute("percentualeFrutta", percentualeFrutta);
        model.addAttribute("percentualeVerdura", percentualeVerdura);
        model.addAttribute("percentualeOrtaggi", percentualeOrtaggi);
        model.addAttribute("totaleprodotti", totaleprodotti);


        // Returning the fragment inside ModelAndView, render the fragment view
        ModelAndView modelAndView = new ModelAndView("/agricolo/donutchart-fragments"); // This will use fragments.html
        return modelAndView;
    }






//    @GetMapping("/agricoltura/json")
//    @ResponseBody
//    public ResponseEntity<List<Prodotto>> getAllProductsJson() {
//        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();
//        List<Double> tonnellateList = new ArrayList<>();
//
//        if (prodotti.getStatusCode().is2xxSuccessful() && prodotti.hasBody()) {
//            return ResponseEntity.ok(prodotti.getBody());
//        } else {
//            logger.error("Failed to fetch products or empty response from agricoloService.");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.emptyList());
//        }
//    }
}
