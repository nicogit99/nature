package com.it.naturlink.service;

import com.it.naturlink.db.mapper.MapperAll;
import com.it.naturlink.naturlink.api.AnimaliApiDelegate;
import com.it.naturlink.naturlink.model.Animale;
import com.it.naturlink.repository.AllevamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllevamentoService implements AnimaliApiDelegate {

    @Autowired
    AllevamentoRepository allevamentoRepository;

    @Override
    public ResponseEntity<List<Animale>> animaliGet() {
        List<Animale> animaleList= MapperAll.INSTANCE.toAnimaleList(allevamentoRepository.findAll());
        return ResponseEntity.ok(animaleList);
    }

    @Override
    public ResponseEntity<Void> animaliIdDelete(Integer id) {
        return AnimaliApiDelegate.super.animaliIdDelete(id);
    }

    @Override
    public ResponseEntity<Animale> animaliIdGet(Integer id) {
        return AnimaliApiDelegate.super.animaliIdGet(id);
    }

    @Override
    public ResponseEntity<Animale> animaliIdPut(Integer id, Animale animale) {
        return AnimaliApiDelegate.super.animaliIdPut(id, animale);
    }


}
