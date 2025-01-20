package com.it.naturlink.controller;

import com.it.naturlink.Utils.Production;
import com.it.naturlink.Utils.Tempo;
import com.it.naturlink.db.mapper.MapperAll;
import com.it.naturlink.naturlink.model.Animale;
import com.it.naturlink.naturlink.model.Prodotto;
import com.it.naturlink.service.AgricoloService;
import com.it.naturlink.service.AllevamentoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class DashboardController {

    @Autowired
    private AgricoloService agricoloService;
    @Autowired
    private AllevamentoService allevamentoService;



    @Autowired
    private Tempo tempo;  // Reuse the same weather object



    @GetMapping("/")
    public ModelAndView homepage() {
        return new ModelAndView("homepage");
    }


    @GetMapping("/agricolo")
    public ModelAndView agricoloPage() {
        return new ModelAndView("/agricolo/agricolo");
    }

    @GetMapping("/allevamento")
    public ModelAndView allevamentoPage() {
        return new ModelAndView("/allevamento/allevamento");
    }

    @GetMapping("/pesca")
    public ModelAndView pescaPage() {
        return new ModelAndView("/pesca/pesca");
    }

    @GetMapping("/sivicoltura")
    public ModelAndView sivicolturaPage() {
        return new ModelAndView("/sivicoltura/sivicoltura");
    }

    @GetMapping("/minerali")
    public ModelAndView mineraliPage() {
        return new ModelAndView("/minerali/minerali");
    }

    @GetMapping("agricolo/datatable-framments")
    public ResponseEntity<Map<String, Object>> getTableFragment() {
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();
        System.out.println(prodotti.getBody());
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int guadagnoperProdotto = 0;
        int totaleprodotti = 0;

        for (Prodotto p : prodotti.getBody()) {
            int superficie = MapperAll.INSTANCE.toAgricolo(p).getSuperficie();
            Integer giorniCrescita = MapperAll.INSTANCE.toAgricolo(p).getGiorniCrescita();

            int tonnellate = Production.calcolaProduzioneAgricola(giorniCrescita, tempo.getTemperatura(), tempo.getPrecipitazioni(), tempo.getUmidita(), superficie);
            tonnellateList.add(tonnellate);
            guadagnoperProdotto = (MapperAll.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
//            totaleprodotti += (AgricoloMapper.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
            tonnellateGuadagno.add(guadagnoperProdotto);
            totaleprodotti += (MapperAll.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
        }

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);

        response.put("tonnellateList", tonnellateList);
        response.put("prodotti", prodotti.getBody());

        // Return ResponseEntity with data and HTTP status

        return ResponseEntity.ok(response);
    }

    @GetMapping("allevamento/datatable-framments")
    public ResponseEntity<Map<String, Object>> getTableFragmentAllevammento() {
        ResponseEntity<List<Animale>> animali = allevamentoService.animaliGet();
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int guadagnoperProdotto = 0;
        int totaleprodotti = 0;

        for (Animale p : animali.getBody()) {

            int tonnellate=Production.calcolaProduzioneAllevamentoAnimali(p.getQuantita(),p.getTipo());
            tonnellateList.add(tonnellate);
            guadagnoperProdotto = (MapperAll.INSTANCE.toAllevamento(p).getPrezzo() * tonnellate);
            tonnellateGuadagno.add(guadagnoperProdotto);
            totaleprodotti += (MapperAll.INSTANCE.toAllevamento(p).getPrezzo() * tonnellate) ;
        }

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);

        response.put("tonnellateList", tonnellateList);
        response.put("animali", animali.getBody());

        // Return ResponseEntity with data and HTTP status

        return ResponseEntity.ok(response);
    }

    @GetMapping("json")
    public ResponseEntity<List<Animale>>Allevamento() {
        if(allevamentoService.animaliGet().getBody()==null){
            System.out.println("nessum valore");
        }
        return ResponseEntity.ok(allevamentoService.animaliGet().getBody());
    }












}
