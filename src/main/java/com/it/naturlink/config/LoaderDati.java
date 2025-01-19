package com.it.naturlink.config;

import com.it.naturlink.Utils.Tempo;
import com.it.naturlink.db.*;
import com.it.naturlink.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    @Scheduled(fixedRate = 10000)
    @Transactional
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
        CompletableFuture<Void> sivicolturaTask = caricaSivicolura();

        // Attendi che tutti i task siano completati
        CompletableFuture.allOf(agricoloTask, allevamentoTask, pesceTask, mineraliTask ,sivicolturaTask ).join();
        //caricaAgricolo();
        //caricaAllevamento();
        //caricaPesca();
        //caricaMinerali();
        //caricaSivicoltura();
    }


    @Async
    private CompletableFuture<Void>caricaAgricolo() {

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

        log.info("Dati aggiunti Agricolo");
        return CompletableFuture.completedFuture(null);
    }

    @Async
    private CompletableFuture<Void>caricaSivicolura() {

        // Liste di frutta, verdura e ortaggi
        List<String> foresteTropicali = Arrays.asList("mogano", "balsa", "palme");
        List<String> foresteTemperate = Arrays.asList("faggio", "castagno", "abete");
        List<String> foresteBoreali= Arrays.asList("pino silestre", "larice", "cipresso");

        Random random = new Random();

        List<Sivicolture> tuttiAlberi = new ArrayList<>();

        tuttiAlberi.addAll(getListaSivicoltura(foresteTropicali, " foreste tropicali", random));
        tuttiAlberi.addAll(getListaSivicoltura(foresteTemperate, "foreste temperate", random));
        tuttiAlberi.addAll(getListaSivicoltura(foresteBoreali, "froeste boreali", random));


        sivicoltureRepository.saveAll(tuttiAlberi);

        log.info("Dati aggiunti Sivicoltura");
        return CompletableFuture.completedFuture(null);
    }




    @Async
    private CompletableFuture<Void>caricaPesca() {

        // Liste di frutta, verdura e ortaggi
        List<String> predatori = Arrays.asList("tonno", "pesce spada", "merluzzo","salmone","spigola");
        List<String>  carapace = Arrays.asList("astice", "aragosta", "gamberetti","scampi","cicala greca");
        List<String>  molluschi = Arrays.asList("cozze", "vongole", "ostriche","","capasanta","zeffiro");
        Random random = new Random();

        List<Pesce> tuttipesci = new ArrayList<>();

        tuttipesci.addAll(getListaPesca(predatori, "predatori", random));
        tuttipesci.addAll(getListaPesca(carapace , "carapace", random));
        tuttipesci.addAll(getListaPesca(molluschi , "molluschi", random));

        pesceRepository.saveAll(tuttipesci);

        log.info("Dati aggiunti Pesca");
        return CompletableFuture.completedFuture(null);
    }

    private static List<Pesce> getListaPesca (List<String> elementi, String categoria, Random random) {
        int prezzo;
        int profondita;
        int stockPesce;
        List<Pesce>  pesceList = new ArrayList<>();
        for (int i = 0; i < elementi.size(); i++) {
            prezzo = 1 + random.nextInt(1000); // Random number between 0 and 10
            profondita = 1 + random.nextInt(2000) ;
            stockPesce=1 + random.nextInt(100);
            pesceList .add(new Pesce(elementi.get(i),categoria,stockPesce,profondita,prezzo));
        }

        return pesceList ;
    }




    @Async
    private CompletableFuture<Void> caricaMinerali() {



        // Liste di frutta, verdura e ortaggi
        List<String> elementiPreziosi = Arrays.asList("oro", "argento", "diamante");
        List<String>  medioPreziosi= Arrays.asList("Carbonio", "Grafite", "Rame");
        List<String> menoPreziosi = Arrays.asList("quarzo", "calcare", "zolfo");

        Random random = new Random();

        List<EstrazioneMineraria> tuttiMinerali = new ArrayList<>();

        tuttiMinerali.addAll(getListaMinerali(elementiPreziosi, "preziosi", random));
        tuttiMinerali.addAll(getListaMinerali(medioPreziosi, "mediopreziosi", random));
        tuttiMinerali.addAll(getListaMinerali(menoPreziosi, "menopreziosi", random));

        estrazioneRepository.saveAll(tuttiMinerali);

        log.info("Dati aggiunti Minerali");
        return CompletableFuture.completedFuture(null);
    }

    private static List<EstrazioneMineraria> getListaMinerali(List<String> elementi, String categoria, Random random) {
        int prezzo;
        int profondita;
        int purezza;
        int quantita;
        List<EstrazioneMineraria> minerariaList = new ArrayList<>();
        for (int i = 0; i < elementi.size(); i++) {
            prezzo = 1 + random.nextInt(1000); // Random number between 0 and 10
            profondita = 1 + random.nextInt(2000) ;
            purezza =1 + random.nextInt(98);
            quantita=1 + random.nextInt(20);
            minerariaList.add(new EstrazioneMineraria(elementi.get(i), categoria,quantita, prezzo, profondita, purezza));
        }

        return minerariaList;
    }







    @Async
    private CompletableFuture<Void> caricaAllevamento() {

        Random random = new Random();

        List<Allevamento> animaliList = new ArrayList<>();
         int prezzobovino = 1 + random.nextInt(6000);
        int quantita = 5 + random.nextInt(15);
        int prezzosuino = 1 + random.nextInt(6000);
        int quantita1 = 5 + random.nextInt(15);
        int prezzovino = 1 + random.nextInt(6000);
        int quantita2 = 5 + random.nextInt(15);
        int prezzopollame = 1 + random.nextInt(6000);
        int quantita3 = 5 + random.nextInt(15);

        animaliList .add(new Allevamento("bovino",prezzobovino,quantita));
        animaliList .add(new Allevamento("suino", prezzosuino, quantita1));
        animaliList .add(new Allevamento("ovino", prezzovino, quantita2));
        animaliList .add(new Allevamento("pollame", prezzopollame, quantita3));


        allevamentoRepository.saveAll(animaliList);

        log.info("Dati aggiunti Allevamento");
        return CompletableFuture.completedFuture(null);
    }






    private static List<Agricolo> getLista(List<String> elementi, String categoria, Random random) {
        int prezzo;
        int superficie;
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

    private static List<Sivicolture> getListaSivicoltura(List<String> elementi, String categoria, Random random) {
        int prezzo;
        int superficie;
        int giorniDiCrescita;
        List<Sivicolture> sivicoltureList = new ArrayList<>();
        for (int i = 0; i < elementi.size(); i++) {
            prezzo = 1 + random.nextInt(4); // Random number between 0 and 10
            superficie = 1 + random.nextInt(10) ;
            giorniDiCrescita = 41 + random.nextInt(79);
            sivicoltureList.add(new Sivicolture(elementi.get(i), categoria, prezzo, superficie, giorniDiCrescita));
        }

        return sivicoltureList;
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

        try {
            // Rimuovi i dati dalle tabelle
            agricoloRepository.deleteAll();
            sivicoltureRepository.deleteAll();
            estrazioneRepository.deleteAll();
            allevamentoRepository.deleteAll();
            pesceRepository.deleteAll();

            // Reset delle sequenze specifiche per ogni tabella
            resetSequenza("sequenza_id_agricolo");
            resetSequenza("sequenza_id_allevamento");
            resetSequenza("sequenza_id_minerali");
            resetSequenza("sequenza_id_pesce");
            resetSequenza("sequenza_id_sivicolture");

        } catch (Exception e) {
            log.error("Errore durante il reset dei dati: " + e.getMessage());
        }
    }

    private void resetSequenza(String nomeSequenza) {
        try {
            String queryStr = "ALTER SEQUENCE " + nomeSequenza + " RESTART WITH 1";
            Query query = entityManager.createNativeQuery(queryStr);
            query.executeUpdate();
            log.info("Sequenza " + nomeSequenza + " resettata con successo.");
        } catch (Exception e) {
            log.error("Errore durante il reset della sequenza " + nomeSequenza + ": " + e.getMessage());
        }
    }

}



