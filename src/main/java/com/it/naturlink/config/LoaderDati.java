package com.it.naturlink.config;

import com.it.naturlink.Utils.Weather;
import com.it.naturlink.db.Agricolo;
import com.it.naturlink.repository.AgricoloRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component

public class LoaderDati {

    private final AgricoloRepository agricoloRepository;
    private final Weather weather;
    private final EntityManager entityManager;

    @Autowired
    public LoaderDati(AgricoloRepository agricoloRepository, EntityManager entityManager, Weather weather) {
        this.agricoloRepository = agricoloRepository;
        this.weather = weather;
        this.entityManager = entityManager;
    }

    @PostConstruct
    @Transactional
    @Scheduled(fixedRate = 6000)
    public void caricamento() {
        // Verifica che i dati meteo siano pronti prima di procedere con il caricamento dei dati agricoli
        if (weather.areDatiPronti()) {
            loadDati(); // Carica i dati agricoli solo se i dati meteo sono pronti
        } else {
            log.warn("I dati meteo non sono ancora pronti. Attendere...");
        }
    }

    private void aggiuntaDati() {
        caricamentoValori();
    }

    private void caricamentoValori() {
        log.info("Generazione dati random");

        // Liste di frutta, verdura e ortaggi
        List<String> frutta = Arrays.asList("pere", "mele", "banane");
        List<String> verdura = Arrays.asList("bietole", "spinaci", "cetriolo");
        List<String> ortaggi = Arrays.asList("pomodoro", "carote", "melanzana");

        Random random = new Random();

        List<Agricolo> tuttiGliAgricoli = new ArrayList<>();

        tuttiGliAgricoli.addAll(getLista(frutta, "frutta", random));
        tuttiGliAgricoli.addAll(getLista(verdura, "verdura", random));
        tuttiGliAgricoli.addAll(getLista(ortaggi, "ortaggi", random));

        agricoloRepository.saveAll(tuttiGliAgricoli);

        log.info("Dati aggiunti");
    }

    private static List<Agricolo> getLista(List<String> elementi, String categoria, Random random) {
        int prezzo;
        int superficie;
        int quantità;
        int giorniDiCrescita;
        List<Agricolo> agricoloList = new ArrayList<>();
        for (int i = 0; i < elementi.size(); i++) {
            prezzo = 1 + random.nextInt(4); // Random number between 0 and 10
            quantità = 1 + random.nextInt(10);
            superficie = 1 + random.nextInt(10) ;
            giorniDiCrescita = 41 + random.nextInt(79);
            agricoloList.add(new Agricolo(elementi.get(i), categoria, quantità, prezzo, superficie, giorniDiCrescita));
        }

        return agricoloList;
    }

    public void loadDati() {
        log.info("Inizio reset e caricamento dati");

        // Se i dati meteo sono pronti, procedi con il reset e il caricamento dei dati agricoli
        if (weather.areDatiPronti()) {
            resetta();
            aggiuntaDati();
        } else {
            log.warn("I dati meteo non sono ancora pronti per il caricamento dei dati agricoli.");
        }

        log.info("Esecuzione Load dati aggiornata");
    }


    private void resetta() {
        log.info("Resettando dati");

        agricoloRepository.deleteAll();
        try {
            Query query = entityManager.createNativeQuery("ALTER SEQUENCE sequenza_id RESTART WITH 1");
            query.executeUpdate();
        } catch (Exception e) {
            log.error("Errore durante il reset della sequenza: " + e.getMessage());
        }
    }
}
