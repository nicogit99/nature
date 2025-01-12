package com.it.naturlink.service;

import com.it.naturlink.db.mapper.MapperAll;
import com.it.naturlink.naturlink.api.ProdottiApiDelegate;
import com.it.naturlink.naturlink.model.Prodotto;
import com.it.naturlink.repository.AgricoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgricoloServiceImpl implements  ProdottiApiDelegate {

    @Autowired
    AgricoloRepository agricoloRepository;

    @Override
    public ResponseEntity<List<Prodotto>> prodottiGet() {
        List<Prodotto> prodottoList= MapperAll.INSTANCE.toProdottoList(agricoloRepository.findAll());
        return ResponseEntity.ok().body(prodottoList);
    }

    @Override
    public ResponseEntity<Void> prodottiIdDelete(Integer id) {
        return ProdottiApiDelegate.super.prodottiIdDelete(id);
    }

    @Override
    public ResponseEntity<Prodotto> prodottiIdGet(Integer id) {
        return ProdottiApiDelegate.super.prodottiIdGet(id);
    }

    @Override
    public ResponseEntity<Prodotto> prodottiIdPut(Integer id, Prodotto prodotto) {
        return ProdottiApiDelegate.super.prodottiIdPut(id, prodotto);
    }

    @Override
    public ResponseEntity<Prodotto> prodottiPost(Prodotto prodotto) {
        return ProdottiApiDelegate.super.prodottiPost(prodotto);
    }
}
