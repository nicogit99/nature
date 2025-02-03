package com.it.naturlink.service;

import com.it.naturlink.db.mapper.MapperAll;
import com.it.naturlink.naturlink.api.PesciApiDelegate;
import com.it.naturlink.naturlink.model.Minerale;
import com.it.naturlink.naturlink.model.Pesca;
import com.it.naturlink.repository.PesceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PescaService  implements PesciApiDelegate {

    @Autowired
    PesceRepository pesceRepository;

    @Override
    public ResponseEntity<List<Pesca>> pesciGet() {
        List<Pesca> pescaList = MapperAll.INSTANCE.toPescaList(pesceRepository.findAll());
        if (pescaList.isEmpty())
            return new ResponseEntity<>(pescaList, HttpStatusCode.valueOf(400));
        return ResponseEntity.ok(pescaList);
    }

    public void pescaP(Pesca pesca) {
        pesceRepository.save(MapperAll.INSTANCE.toPesce(pesca));
    }

    public void delete(int id) {
        pesceRepository.deleteById(id);
    }
}
