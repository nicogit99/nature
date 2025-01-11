package com.it.naturlink.controller;

import com.it.naturlink.Utils.Production;
import com.it.naturlink.Utils.Weather;
import com.it.naturlink.db.mapper.AgricoloMapper;
import com.it.naturlink.naturlink.model.Prodotto;
import com.it.naturlink.service.AgricoloServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            totaleprodotti += (AgricoloMapper.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
        }

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);

        response.put("tonnellateList", tonnellateList);
        response.put("prodotti", prodotti.getBody());

        // Return ResponseEntity with data and HTTP status

        return ResponseEntity.ok(response);
    }


}
