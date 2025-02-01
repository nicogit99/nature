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
        CompletableFuture<Void> sivicolturaTask = caricaSivicoltura();

        CompletableFuture.allOf(agricoloTask, allevamentoTask, pesceTask, mineraliTask ,sivicolturaTask ).join();

    }


    @Async
    private CompletableFuture<Void> caricaAgricolo() {
        List<Agricolo> tuttiGliAgricoli = agricoloRepository.findAll(); // Recupera tutti gli agricoli esistenti

        if (tuttiGliAgricoli.isEmpty()) {
            // Liste di frutta, verdura e ortaggi
            List<String> frutta = Arrays.asList("pere", "mele", "banane");
            List<String> verdura = Arrays.asList("bietole", "spinaci", "cetriolo");
            List<String> ortaggi = Arrays.asList("pomodoro", "carote", "melanzana");

            Random random = new Random();

            // Popola il database con nuovi dati
            tuttiGliAgricoli.addAll(getLista(frutta, "frutta", random));
            tuttiGliAgricoli.addAll(getLista(verdura, "verdura", random));
            tuttiGliAgricoli.addAll(getLista(ortaggi, "ortaggi", random));

            // Salva tutti gli agricoli nel database
            agricoloRepository.saveAll(tuttiGliAgricoli);

            return CompletableFuture.completedFuture(null);
        } else {
            Random random = new Random();

            // Aggiornamento dei dati agricoli esistenti
            for (Agricolo agricolo : tuttiGliAgricoli) {
                Agricolo existingAgricolo = agricoloRepository.findById(agricolo.getId()).orElse(null);
                if (existingAgricolo != null) {
                    // Aggiorna i dati solo se l'agricolo esiste
                    int prezzo = 1 + random.nextInt(4); // Random number between 1 and 4
                    int superficie = 1 + random.nextInt(10);
                    int giorniDiCrescita = 41 + random.nextInt(79);

                    existingAgricolo.setPrezzo(prezzo);
                    existingAgricolo.setGiorniCrescita(giorniDiCrescita);

                    // Salva l'agricolo aggiornato
                    agricoloRepository.save(existingAgricolo);
                }
            }

            return CompletableFuture.completedFuture(null);
        }
    }

    @Async
    private CompletableFuture<Void> caricaSivicoltura() {
        List<Sivicolture> tuttiGliAlberi = sivicoltureRepository.findAll(); // Recupera tutte le entità di tipo Sivicoltura esistenti

        if (tuttiGliAlberi.isEmpty()) {
            // Liste di alberi per vari tipi di foreste
            List<String> foresteTropicali = Arrays.asList("mogano", "balsa", "palme");
            List<String> foresteTemperate = Arrays.asList("faggio", "castagno", "abete");
            List<String> foresteBoreali = Arrays.asList("pino silestre", "larice", "cipresso");

            Random random = new Random();

            // Popola il database con nuovi alberi
            tuttiGliAlberi.addAll(getListaSivicoltura(foresteTropicali, "foreste tropicali", random));
            tuttiGliAlberi.addAll(getListaSivicoltura(foresteTemperate, "foreste temperate", random));
            tuttiGliAlberi.addAll(getListaSivicoltura(foresteBoreali, "foreste boreali", random));

            // Salva tutte le entità Sivicoltura nel database
            sivicoltureRepository.saveAll(tuttiGliAlberi);

            log.info("Dati aggiunti Sivicoltura");
            return CompletableFuture.completedFuture(null);
        } else {
            Random random = new Random();

            // Aggiornamento dei dati esistenti per Sivicoltura
            for (Sivicolture albero : tuttiGliAlberi) {
                Sivicolture existingAlbero = sivicoltureRepository.findById(albero.getId()).orElse(null);
                if (existingAlbero != null) {
                    // Aggiorna i dati solo se l'albero esiste
                    int prezzo = 1 + random.nextInt(2000);
                    int superficie = 1 + random.nextInt(10);
                    int giorniDiCrescita = 41 + random.nextInt(79);

                    existingAlbero.setPrezzo(prezzo);
                    existingAlbero.setGiorniCrescita(giorniDiCrescita);

                    // Salva l'albero aggiornato
                    sivicoltureRepository.save(existingAlbero);
                }
            }

            return CompletableFuture.completedFuture(null);
        }
    }

    @Async
    private CompletableFuture<Void> caricaPesca() {
        List<Pesce> tuttiIPesci = pesceRepository.findAll(); // Recupera tutti i pesci esistenti

        if (tuttiIPesci.isEmpty()) {
            // Liste di pesci per categorie
            List<String> predatori = Arrays.asList("tonno", "pesce spada", "merluzzo", "salmone", "spigola");
            List<String> carapace = Arrays.asList("astice", "aragosta", "gamberetti", "scampi", "cicala greca");
            List<String> molluschi = Arrays.asList("cozze", "vongole", "ostriche", "capasanta", "zeffiro");

            Random random = new Random();

            // Popola il database con nuovi pesci
            tuttiIPesci.addAll(getListaPesca(predatori, "predatori", random));
            tuttiIPesci.addAll(getListaPesca(carapace, "carapace", random));
            tuttiIPesci.addAll(getListaPesca(molluschi, "molluschi", random));

            // Salva tutti i pesci nel database
            pesceRepository.saveAll(tuttiIPesci);

            log.info("Dati aggiunti Pesca");
            return CompletableFuture.completedFuture(null);
        } else {
            Random random = new Random();

            // Aggiornamento dei dati esistenti per i pesci
            for (Pesce pesce : tuttiIPesci) {
                Pesce existingPesce = pesceRepository.findById(pesce.getId()).orElse(null);
                if (existingPesce != null) {
                    // Aggiorna i dati solo se il pesce esiste
                    int prezzo = 1 + random.nextInt(15); // Random number between 1 and 300
                    int profondita = 600 + random.nextInt(2000);
                    int stockPesce = 50 + random.nextInt(100);

                    existingPesce.setPrezzo(prezzo);
                    existingPesce.setProfondita(profondita);
                    existingPesce.setStockPesce(stockPesce);

                    // Salva il pesce aggiornato
                    pesceRepository.save(existingPesce);
                }
            }

            return CompletableFuture.completedFuture(null);
        }
    }

    @Async
    private CompletableFuture<Void> caricaMinerali() {
        List<EstrazioneMineraria> tuttiIMinerali = estrazioneRepository.findAll(); // Recupera tutti i minerali esistenti

        if (tuttiIMinerali.isEmpty()) {
            // Liste di minerali per categorie
            List<String> mineraliPreziosi = Arrays.asList("oro", "argento", "diamante");
            List<String> mineraliMedi = Arrays.asList("carbonio", "grafite", "rame");
            List<String> mineraliMenoPreziosi = Arrays.asList("quarzo", "calcare", "zolfo");

            Random random = new Random();

            // Popola il database con nuovi minerali
            tuttiIMinerali.addAll(getListaMinerali(mineraliPreziosi, "preziosi", random));
            tuttiIMinerali.addAll(getListaMinerali(mineraliMedi, "mediopreziosi", random));
            tuttiIMinerali.addAll(getListaMinerali(mineraliMenoPreziosi, "menopreziosi", random));

            // Salva tutti i minerali nel database
            estrazioneRepository.saveAll(tuttiIMinerali);

            log.info("Dati aggiunti Minerali");
            return CompletableFuture.completedFuture(null);
        } else {
            Random random = new Random();

            // Aggiornamento dei dati esistenti per i minerali
            for (EstrazioneMineraria minerale : tuttiIMinerali) {
                EstrazioneMineraria existingMinerale = estrazioneRepository.findById(minerale.getId()).orElse(null);
                if (existingMinerale != null) {
                    // Aggiorna i dati solo se il minerale esiste
                    int prezzo = 200 + random.nextInt(1000);
                    int profondita = 600 + random.nextInt(2000);
                    int purezza = 30 + random.nextInt(61); // random.nextInt(61) genera un numero tra 30 e 90
                    int quantita = 1 + random.nextInt(20);

                    existingMinerale.setPrezzo(prezzo);
                    existingMinerale.setProfondita(profondita);
                    existingMinerale.setPurezza(purezza);
                    existingMinerale.setQuantita(quantita);

                    // Salva il minerale aggiornato
                    estrazioneRepository.save(existingMinerale);
                }
            }

            return CompletableFuture.completedFuture(null);
        }
    }

    @Async
    private CompletableFuture<Void> caricaAllevamento() {
        List<Allevamento> tuttiGliAnimali = allevamentoRepository.findAll(); // Recupera tutti gli animali esistenti

        if (tuttiGliAnimali.isEmpty()) {
            Random random = new Random();

            // Popola il database con nuovi animali da allevamento
            tuttiGliAnimali.add(new Allevamento("bovino", 1 + random.nextInt(1000), 5 + random.nextInt(6)));
            tuttiGliAnimali.add(new Allevamento("suino", 1 + random.nextInt(2000), 5 + random.nextInt(6)));
            tuttiGliAnimali.add(new Allevamento("ovino", 1 + random.nextInt(1000), 5 + random.nextInt(10)));
            tuttiGliAnimali.add(new Allevamento("pollame", 1 + random.nextInt(500), 5 + random.nextInt(10)));

            // Salva tutti gli animali nel database
            allevamentoRepository.saveAll(tuttiGliAnimali);

            log.info("Dati aggiunti Allevamento");
            return CompletableFuture.completedFuture(null);
        } else {
            Random random = new Random();

            // Aggiornamento dei dati esistenti per gli animali
            for (Allevamento animale : tuttiGliAnimali) {
                Allevamento existingAnimale = allevamentoRepository.findById(animale.getId()).orElse(null);
                if (existingAnimale != null) {
                    // Aggiorna i dati solo se l'animale esiste
                    int prezzo = 1 + random.nextInt(1000);
                    int quantita = 5 + random.nextInt(10);

                    existingAnimale.setPrezzo(prezzo);
                    existingAnimale.setQuantita(quantita);

                    // Salva l'animale aggiornato
                    allevamentoRepository.save(existingAnimale);
                }
            }

            return CompletableFuture.completedFuture(null);
        }
    }




    private static List<Pesce> getListaPesca (List<String> elementi, String categoria, Random random) {
        int prezzo;
        int profondita;
        int stockPesce;
        List<Pesce>  pesceList = new ArrayList<>();
        for (int i = 0; i < elementi.size(); i++) {
            prezzo = 1 + random.nextInt(300); // Random number between 0 and 10
            profondita = 600 + random.nextInt(2000) ;
            stockPesce=50 + random.nextInt(100);
            pesceList.add(new Pesce(elementi.get(i),categoria,stockPesce,profondita,prezzo));
        }

        return pesceList ;
    }


    private static List<EstrazioneMineraria> getListaMinerali(List<String> elementi, String categoria, Random random) {
        int prezzo;
        int profondita;
        int purezza;
        int quantita;
        List<EstrazioneMineraria> minerariaList = new ArrayList<>();
        for (int i = 0; i < elementi.size(); i++) {
            prezzo = 200 + random.nextInt(1000); // Random number between 0 and 10
            profondita = 600 + random.nextInt(2000) ;
            purezza = 30 + random.nextInt(61); // random.nextInt(71) genera un numero tra 0 e 70, che sommato a 20 dà un valore tra 20 e 90.
            quantita=1 + random.nextInt(20);
            minerariaList.add(new EstrazioneMineraria(elementi.get(i), categoria,quantita, prezzo, profondita, purezza));
        }

        return minerariaList;
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
            prezzo = 1 + random.nextInt(2000); // Random number between 0 and 10
            superficie = 1 + random.nextInt(10) ;
            giorniDiCrescita = 41 + random.nextInt(79);
            sivicoltureList.add(new Sivicolture(elementi.get(i), categoria, prezzo, superficie, giorniDiCrescita));
        }

        return sivicoltureList;
    }



    @Scheduled(fixedDelay = 10000)
    public void loadDati() {
        log.info("Inizio reset e caricamento dati");

        // Se i dati meteo sono pronti, procedi con il reset e il caricamento dei dati
        if (tempo.areDatiPronti()) {
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


