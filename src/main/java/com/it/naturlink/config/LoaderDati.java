package com.it.naturlink.config;

import com.it.naturlink.Utils.Tempo;
import com.it.naturlink.db.Agricolo;
import com.it.naturlink.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@EnableAsync
public class LoaderDati {

    private final AgricoloRepository agricoloRepository;
    private final AllevamentoRepository allevamentoRepository;
    private  final EstrazioneRepository estrazioneRepository;
    private final PesceRepository pesceRepository;
    private  final SivicoltureRepository sivicoltureRepository;

    private final Tempo tempo;
    private final EntityManager entityManager;

    @Autowired
    public LoaderDati(SivicoltureRepository sivicoltureRepository,PesceRepository pesceRepository,EstrazioneRepository estrazioneRepository,AgricoloRepository agricoloRepository, EntityManager entityManager, Tempo tempo,AllevamentoRepository allevamentoRepository) {
        this.agricoloRepository = agricoloRepository;
        this.tempo = tempo;
        this.entityManager = entityManager;
        this.allevamentoRepository=allevamentoRepository;
        this.pesceRepository=pesceRepository;
        this.estrazioneRepository=estrazioneRepository;
        this.sivicoltureRepository=sivicoltureRepository;
    }

    @PostConstruct
    @Transactional
    @Scheduled(fixedRate = 6000)
    public void caricamento() {
        // Verifica che i dati meteo siano pronti prima di procedere con il caricamento dei dati agricoli
        if (tempo.areDatiPronti()) {
            loadDati(); // Carica i dati agricoli solo se i dati meteo sono pronti
        } else {
            log.warn("I dati meteo non sono ancora pronti. Attendere...");
        }
    }

    private void aggiuntaDati() {
        caricaAgricolo();
        caricaAllevamento();
        caricaPesca();
        caricaMinerali();
        caricaSivicoltura();
    }

    @Async
    private void caricaAgricolo() {
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
            superficie = 1 + random.nextInt(10) ;
            giorniDiCrescita = 41 + random.nextInt(79);
            agricoloList.add(new Agricolo(elementi.get(i), categoria, prezzo, superficie, giorniDiCrescita));
        }

        return agricoloList;
    }

    public void loadDati() {
        log.info("Inizio reset e caricamento dati");

        // Se i dati meteo sono pronti, procedi con il reset e il caricamento dei dati
        if (tempo.areDatiPronti()) {
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
        sivicoltureRepository.deleteAll();
        estrazioneRepository.deleteAll();
        allevamentoRepository.deleteAll();
        pesceRepository.deleteAll();
        try {
            Query query = entityManager.createNativeQuery("ALTER SEQUENCE sequenza_id RESTART WITH 1");
            query.executeUpdate();
        } catch (Exception e) {
            log.error("Errore durante il reset della sequenza: " + e.getMessage());
        }
    }
}
/*
package com.it.naturlink.config;

import com.it.naturlink.Utils.Tempo;
import com.it.naturlink.db.Agricolo;
import com.it.naturlink.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@EnableAsync
public class LoaderDati {

    private final AgricoloRepository agricoloRepository;
    private final AllevamentoRepository allevamentoRepository;
    private final EstrazioneRepository estrazioneRepository;
    private final PesceRepository pesceRepository;
    private final SivicoltureRepository sivicoltureRepository;

    private final Tempo tempo;
    private final EntityManager entityManager;

    @Autowired
    public LoaderDati(SivicoltureRepository sivicoltureRepository, PesceRepository pesceRepository,
                      EstrazioneRepository estrazioneRepository, AgricoloRepository agricoloRepository,
                      EntityManager entityManager, Tempo tempo, AllevamentoRepository allevamentoRepository) {
        this.agricoloRepository = agricoloRepository;
        this.tempo = tempo;
        this.entityManager = entityManager;
        this.allevamentoRepository = allevamentoRepository;
        this.pesceRepository = pesceRepository;
        this.estrazioneRepository = estrazioneRepository;
        this.sivicoltureRepository = sivicoltureRepository;
    }

    @PostConstruct
    @Scheduled(fixedRate = 6000)
    public void caricamento() {
        // Verifica che i dati meteo siano pronti prima di procedere con il caricamento dei dati agricoli
        if (tempo.areDatiPronti()) {
            loadDati(); // Carica i dati agricoli solo se i dati meteo sono pronti
        } else {
            log.warn("I dati meteo non sono ancora pronti. Attendere...");
        }
    }

    private void aggiuntaDati() {
        // Esegui i caricamenti asincroni
        CompletableFuture<Void> agricoloTask = caricaAgricolo();
        CompletableFuture<Void> allevamentoTask = caricaAllevamento();
        CompletableFuture<Void> pesceTask = caricaPesca();
        CompletableFuture<Void> mineraliTask = caricaMinerali();
        CompletableFuture<Void> sivicolturaTask = caricaSivicoltura();

        // Attendi che tutti i task siano completati
        CompletableFuture.allOf(agricoloTask, allevamentoTask, pesceTask, mineraliTask, sivicolturaTask).join();
    }

    @Async
    private CompletableFuture<Void> caricaAgricolo() {
        log.info("Generazione dati random per Agricolo");

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

        log.info("Dati Agricolo aggiunti");

        return CompletableFuture.completedFuture(null);
    }

    @Async
    private CompletableFuture<Void> caricaAllevamento() {
        // Implementazione del caricamento asincrono per "Allevamento"
        log.info("Caricamento dati Allevamento");

        // Inserisci la logica per caricare i dati di allevamento

        log.info("Dati Allevamento aggiunti");
        return CompletableFuture.completedFuture(null);
    }

    @Async
    private CompletableFuture<Void> caricaPesca() {
        // Implementazione del caricamento asincrono per "Pesca"
        log.info("Caricamento dati Pesca");

        // Inserisci la logica per caricare i dati di pesca

        log.info("Dati Pesca aggiunti");
        return CompletableFuture.completedFuture(null);
    }

    @Async
    private CompletableFuture<Void> caricaMinerali() {
        // Implementazione del caricamento asincrono per "Minerali"
        log.info("Caricamento dati Minerali");

        // Inserisci la logica per caricare i dati minerali

        log.info("Dati Minerali aggiunti");
        return CompletableFuture.completedFuture(null);
    }

    @Async
    private CompletableFuture<Void> caricaSivicoltura() {
        // Implementazione del caricamento asincrono per "Sivicoltura"
        log.info("Caricamento dati Sivicoltura");

        // Inserisci la logica per caricare i dati di sivicoltura

        log.info("Dati Sivicoltura aggiunti");
        return CompletableFuture.completedFuture(null);
    }

    public void loadDati() {
        log.info("Inizio reset e caricamento dati");

        // Se i dati meteo sono pronti, procedi con il reset e il caricamento dei dati
        if (tempo.areDatiPronti()) {
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
        sivicoltureRepository.deleteAll();
        estrazioneRepository.deleteAll();
        allevamentoRepository.deleteAll();
        pesceRepository.deleteAll();
        try {
            Query query = entityManager.createNativeQuery("ALTER SEQUENCE sequenza_id RESTART WITH 1");
            query.executeUpdate();
        } catch (Exception e) {
            log.error("Errore durante il reset della sequenza: " + e.getMessage());
        }
    }
}





 */