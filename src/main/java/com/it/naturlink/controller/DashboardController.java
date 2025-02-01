package com.it.naturlink.controller;

import com.it.naturlink.Utils.Production;
import com.it.naturlink.Utils.Tempo;
import com.it.naturlink.db.Agricolo;
import com.it.naturlink.db.EstrazioneMineraria;
import com.it.naturlink.db.mapper.MapperAll;
import com.it.naturlink.naturlink.model.*;
import com.it.naturlink.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private EstrazioneService estrazioneService;

    @Autowired
    private PescaService pescaService;

    @Autowired
    private SivicolturaService sivicolturaService;

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
    public ResponseEntity<Map<String, Object>> getTableFragmentAgricolo() {
        ResponseEntity<List<Prodotto>> prodotti = agricoloService.prodottiGet();
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int guadagnoperProdotto = 0;
        int totaleprodotti = 0;

        for (Prodotto p : prodotti.getBody()) {
            int superficie = MapperAll.INSTANCE.toAgricolo(p).getSuperficie();
            Integer giorniCrescita = MapperAll.INSTANCE.toAgricolo(p).getGiorniCrescita();
            if (giorniCrescita != null) {
                int tonnellate = Production.calcolaProduzioneAgricola(giorniCrescita, tempo.getTemperatura(), tempo.getPrecipitazioni(), tempo.getUmidita(), superficie);
                tonnellateList.add(tonnellate);
                guadagnoperProdotto = (MapperAll.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
                tonnellateGuadagno.add(guadagnoperProdotto);
                totaleprodotti += (MapperAll.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
            } else {
                int tonnellate = Production.calcolaProduzioneAgricola(0, tempo.getTemperatura(), tempo.getPrecipitazioni(), tempo.getUmidita(), superficie);
                tonnellateList.add(tonnellate);
                guadagnoperProdotto = (MapperAll.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
                tonnellateGuadagno.add(guadagnoperProdotto);
                totaleprodotti += (MapperAll.INSTANCE.toAgricolo(p).getPrezzo() * 1000) * tonnellate;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);
        response.put("tonnellateList", tonnellateList);
        response.put("prodotti", prodotti.getBody());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/submitFormAgricolo")
    public ModelAndView submitFormAgricolo(@ModelAttribute Prodotto agricolo, Model model) {
        agricoloService.prodottoPost(agricolo);
        model.addAttribute("agricolo", "salvato");
        return new ModelAndView("redirect:/agricolo");
    }

    @PostMapping("/submitFormAllevamento")
    public ModelAndView submitFormAllevamento(@ModelAttribute Animale animale, Model model) {
        allevamentoService.animalePost(animale);
        model.addAttribute("allevamento", "salvato");
        return new ModelAndView("redirect:/allevamento");
    }

    @PostMapping("/submitFormPesca")
    public ModelAndView submitFormPesca(@ModelAttribute Pesca pesca, Model model) {
        pescaService.pescaPost(pesca);
        model.addAttribute("pesca", "salvato");
        return new ModelAndView("redirect:/pesca");
    }

    @PostMapping("/submitFormSivicoltura")
    public ModelAndView submitFormSivicoltura(@ModelAttribute Sivicoltura sivicoltura, Model model) {
        sivicolturaService.sivicolturaPost(sivicoltura);
        model.addAttribute("sivicoltura", "salvato");
        return new ModelAndView("redirect:/sivicoltura");
    }

    @PostMapping("/submitFormMinerali")
    public ModelAndView submitFormMinerali(@ModelAttribute Minerale minerale, Model model) {
        estrazioneService.mineralePost(minerale);
        model.addAttribute("minerale", "salvato");
        return new ModelAndView("redirect:/minerali");
    }

    @GetMapping("pesca/datatable-framments")
    public ResponseEntity<Map<String, Object>> getTableFragmentPesca() {
        ResponseEntity<List<Pesca>> pesci = pescaService.pesciGet();
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int guadagnoperProdotto = 0;
        int totaleprodotti = 0;

        for (Pesca p : pesci.getBody()) {
            int profondita = MapperAll.INSTANCE.toPesce(p).getProfondita();
            Integer stockpesci = MapperAll.INSTANCE.toPesce(p).getStockPesce();

            if (stockpesci != null) {
                int tonnellate = Production.calcolaProduzionePesca(tempo.getTemperatura(), profondita, stockpesci);
                tonnellateList.add(tonnellate);
                guadagnoperProdotto = (MapperAll.INSTANCE.toPesce(p).getPrezzo() * tonnellate);
                tonnellateGuadagno.add(guadagnoperProdotto);
                totaleprodotti += (MapperAll.INSTANCE.toPesce(p).getPrezzo() * tonnellate);
            } else {
                int tonnellate = Production.calcolaProduzionePesca(tempo.getTemperatura(), profondita, 0);
                tonnellateList.add(tonnellate);
                guadagnoperProdotto = (MapperAll.INSTANCE.toPesce(p).getPrezzo() * tonnellate);
                tonnellateGuadagno.add(guadagnoperProdotto);
                totaleprodotti += (MapperAll.INSTANCE.toPesce(p).getPrezzo() * tonnellate);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);
        response.put("tonnellateList", tonnellateList);
        response.put("pesci", pesci.getBody());

        return ResponseEntity.ok(response);
    }

    @GetMapping("sivicoltura/datatable-framments")
    public ResponseEntity<Map<String, Object>> getTableFragmentSivicoltura() {
        ResponseEntity<List<Sivicoltura>> sivicoltura = sivicolturaService.sivicolturaGet();
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int guadagnoperProdotto = 0;
        int totaleprodotti = 0;

        for (Sivicoltura p : sivicoltura.getBody()) {
            int superficie = MapperAll.INSTANCE.toSivicolture(p).getSuperficie();
            Integer giorniCrescita = MapperAll.INSTANCE.toSivicolture(p).getGiorniCrescita();

            int tonnellate = Production.calcolaProduzioneSilvicoltura(superficie, giorniCrescita, tempo.getPrecipitazioni(), tempo.getUmidita(), tempo.getTemperatura());
            tonnellateList.add(tonnellate);
            guadagnoperProdotto = (MapperAll.INSTANCE.toSivicolture(p).getPrezzo() * tonnellate);
            tonnellateGuadagno.add(guadagnoperProdotto);
            totaleprodotti += (MapperAll.INSTANCE.toSivicolture(p).getPrezzo() * tonnellate);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);
        response.put("tonnellateList", tonnellateList);
        response.put("sivicoltura", sivicoltura.getBody());

        return ResponseEntity.ok(response);
    }

    @GetMapping("allevamento/datatable-framments")
    public ResponseEntity<Map<String, Object>> getTableFragmentAllevamento() {
        ResponseEntity<List<Animale>> animali = allevamentoService.animaliGet();
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int guadagnoperProdotto = 0;
        int totaleprodotti = 0;

        for (Animale p : animali.getBody()) {
            int tonnellate = Production.calcolaProduzioneAllevamentoAnimali(p.getQuantita(), p.getTipo());
            tonnellateList.add(tonnellate);
            guadagnoperProdotto = (MapperAll.INSTANCE.toAllevamento(p).getPrezzo() * tonnellate);
            tonnellateGuadagno.add(guadagnoperProdotto);
            totaleprodotti += (MapperAll.INSTANCE.toAllevamento(p).getPrezzo() * tonnellate);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);
        response.put("tonnellateList", tonnellateList);
        response.put("animali", animali.getBody());

        return ResponseEntity.ok(response);
    }

    @GetMapping("minerali/datatable-framments")
    public ResponseEntity<Map<String, Object>> getTableFragmentMinerali() {
        ResponseEntity<List<Minerale>> minerali = estrazioneService.mineraliGet();
        List<Integer> tonnellateList = new ArrayList<>();
        List<Integer> tonnellateGuadagno = new ArrayList<>();
        int guadagnoperProdotto = 0;
        int totaleprodotti = 0;

        for (Minerale p : minerali.getBody()) {
            int tonnellate = Production.calcolaProduzioneMineraria(p.getQuantita(), p.getPurezza(), p.getProfondita());
            tonnellateList.add(tonnellate);
            guadagnoperProdotto = (MapperAll.INSTANCE.toEstrazioneMineraria(p).getPrezzo() * tonnellate);
            tonnellateGuadagno.add(guadagnoperProdotto);
            totaleprodotti += (MapperAll.INSTANCE.toEstrazioneMineraria(p).getPrezzo() * tonnellate);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("tonnellateGuadagno", tonnellateGuadagno);
        response.put("tonnellateList", tonnellateList);
        response.put("minerali", minerali.getBody());

        return ResponseEntity.ok(response);
    }

    @GetMapping("json")
    public ResponseEntity<List<Pesca>> Sivicoltura() {
        if (pescaService.pesciGet() == null) {
            System.out.println("nessun valore");
        }
        return ResponseEntity.ok(pescaService.pesciGet().getBody());
    }
}