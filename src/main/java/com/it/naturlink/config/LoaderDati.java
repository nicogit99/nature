package com.it.naturlink.config;

import com.it.naturlink.Utils.Weather;
import com.it.naturlink.db.Agricolo;
import com.it.naturlink.repository.AgricoloRepository;
import com.it.naturlink.service.WeatherService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoaderDati {

    private final AgricoloRepository agricoloRepository;
    private final WeatherService weatherService;


    private EntityManager entityManager;

    @Autowired
    public LoaderDati(AgricoloRepository agricoloRepository, EntityManager entityManager, WeatherService weatherService) {
        this.agricoloRepository = agricoloRepository;
        this.entityManager = entityManager;
        this.weatherService = weatherService;
    }

    @PostConstruct
    public void caricamento() {

        loadDati();
    }

    private void aggiuntaDati() {
        caricamentoValori();
    }

    private void caricamentoValori() {
        log.info("Generazione dati random");


        // Liste di frutta, verdura e ortaggi
        List<String> frutta = Arrays.asList("pere", "mele", "banane", "arancie", "ciliegie", "pesche");
        List<String> verdura = Arrays.asList("bietole", "spinaci", "cetriolo", "cavolo", "zucchine", "verza");
        List<String> ortaggi = Arrays.asList("pomodoro", "carote", "melanzana", "peperone", "patata", "rapa");

        Random random = new Random();
        Integer quantità;
        float prezzo;
        float superficie;


        List<Agricolo> agricoloListFrutta = new ArrayList<>();
        for (int i = 0; i < frutta.size(); i++) {
            prezzo = 0.5f + random.nextFloat() * 100.5f;
            quantità = 1 + random.nextInt(10);
            superficie = 10 + random.nextFloat() * 90;
            agricoloListFrutta.add(new Agricolo(frutta.get(i), "frutta", quantità, prezzo, superficie));

        }
        List<Agricolo> agricoloListVerdura = new ArrayList<>();
        for (int i = 0; i < verdura.size(); i++) {
            prezzo = 0.5f + random.nextFloat() * 100.5f;
            quantità = 1 + random.nextInt(10);
            superficie = 10 + random.nextFloat() * 90;
            agricoloListVerdura.add(new Agricolo(verdura.get(i), "verdura", quantità, prezzo, superficie));

        }
        List<Agricolo> agricoloListOrtaggi = new ArrayList<>();
        for (int i = 0; i < ortaggi.size(); i++) {
            prezzo = 0.5f + random.nextFloat() * 100.5f;
            quantità = 1 + random.nextInt(10);
            superficie = 10 + random.nextFloat() * 90;
            agricoloListOrtaggi.add(new Agricolo(ortaggi.get(i), "ortaggio", quantità, prezzo, superficie));

        }

        List<Agricolo> tuttiGliAgricoli = new ArrayList<>();
        tuttiGliAgricoli.addAll(agricoloListFrutta);
        tuttiGliAgricoli.addAll(agricoloListVerdura);
        tuttiGliAgricoli.addAll(agricoloListOrtaggi);



        agricoloRepository.saveAll(tuttiGliAgricoli);

        log.info("Dati aggiunti");

    }


    @Transactional
    @Scheduled(fixedRate = 5000)
    public void loadDati() {
        log.info("Inizio reset e caricamento dati");
        resetta();
        aggiuntaDati();
        log.info("Esecuzione Load dati aggiornata");
    }

    @Transactional
    private void resetta() {
        log.info("Resettando dati");

        agricoloRepository.deleteAll();
        try {
            Query query = entityManager.createNativeQuery("ALTER SEQUENCE sequenza_id RESTART WITH 1");
            query.executeUpdate();
            log.info("Sequenza resettata");
        } catch (Exception e) {
            log.error("Errore durante il reset della sequenza: " + e.getMessage());
        }
    }


}
